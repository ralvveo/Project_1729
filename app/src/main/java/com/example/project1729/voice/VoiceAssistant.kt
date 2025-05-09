package com.example.project1729.voice

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class VoiceAssistant(
    private val context: Context,
    private var callback: VoiceCallback
)
{
    interface VoiceCallback {
        fun onVoiceCommandRecognized(command: String)
        fun onVoiceTextRecognized(text: String, type: String)
        fun onError(error: String)
        fun onMessage(message: String) // Новый метод для сообщений
    }

    // Audio configuration
    private var audioRecord: AudioRecord? = null
    private val executor = Executors.newSingleThreadExecutor()
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    private val targetBytesPer3Sec = 5 * sampleRate * 2 // 3 секунды в байтах
    private val isRecording = AtomicBoolean(false)
    // API configuration
    private val apiClient by lazy {
        Retrofit.Builder()
            .baseUrl("http://185.207.0.128:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VoiceApiService::class.java)
    }

    fun start() {
        if (isRecording.get()) return
        isRecording.set(true)
        startRecording()
    }

    fun updateCallback(newCallback: VoiceCallback) {
        this.callback = newCallback
    }

    fun stop() {
        if (!isRecording.get()) return
        isRecording.set(false)
        stopRecording()
    }

    fun isRecording() = isRecording.get()

    private fun startRecording() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback.onError("Permission denied")
            return
        }

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        audioRecord?.startRecording()

        executor.execute {
            val buffer = ByteArray(bufferSize)
            val accumulatedData = ByteArrayOutputStream()

            while (isRecording.get()) {
                val bytesRead = audioRecord?.read(buffer, 0, bufferSize) ?: 0
                if (bytesRead > 0) {
                    accumulatedData.write(buffer, 0, bytesRead)
                    processAccumulatedData(accumulatedData)
                }
            }

            accumulatedData.close()
        }
    }

    private fun stopRecording() {
        audioRecord?.apply {
            try {
                stop()
                release()
            } catch (e: IllegalStateException) {
                Log.e("AudioRecord", "Error stopping recording", e)
                callback.onError("Error stopping recording")
            }
        }
        audioRecord = null
    }

    private fun createWavHeader(dataSize: Int): ByteArray {
        val header = ByteArray(44)
        val byteRate = sampleRate * 2 // 16-bit mono

        header.setString(0, "RIFF")
        header.setInt(4, 36 + dataSize)
        header.setString(8, "WAVE")
        header.setString(12, "fmt ")
        header.setInt(16, 16)
        header.setShort(20, 1.toShort())
        header.setShort(22, 1.toShort())
        header.setInt(24, sampleRate)
        header.setInt(28, byteRate)
        header.setShort(32, 2.toShort())
        header.setShort(34, 16.toShort())
        header.setString(36, "data")
        header.setInt(40, dataSize)

        return header
    }

    private fun uploadAudioFile(file: File) {
        val requestFile = RequestBody.create(MediaType.parse("audio/wav"), file)

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        apiClient.uploadAudio(body).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { serverResponse ->
                        if (serverResponse.status == "success") {
                            handleRecognitionResult(serverResponse.result)
                        } else {
                            callback.onError("Server returned unsuccessful status")
                        }
                    } ?: callback.onError("Empty server response")
                } else {
                    callback.onError("Server error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                callback.onError("Network error: ${t.message}")
            }
        })
    }

    private fun handleRecognitionResult(result: RecognitionResult) {
        if (result.recognized && result.item != null && result.item.isNotEmpty()) {
            when (result.type) {
                "command" -> callback.onVoiceCommandRecognized(result.item)
                else -> callback.onVoiceTextRecognized(result.item, result.type ?: "unknown")
            }
        } else {
            callback.onMessage("Говорите громче")
        }
    }

    private fun saveAndUploadChunk(chunk: ByteArray) {
        val wavFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "chunk_${System.currentTimeMillis()}.wav"
        )

        FileOutputStream(wavFile).use { fos ->
            fos.write(createWavHeader(chunk.size))
            fos.write(chunk)
        }

        uploadAudioFile(wavFile)
    }

    private fun processAccumulatedData(data: ByteArrayOutputStream) {
        while (data.size() >= targetBytesPer3Sec) {
            val chunk = data.toByteArray().copyOf(targetBytesPer3Sec)
            saveAndUploadChunk(chunk)
            val remaining = data.toByteArray().copyOfRange(
                targetBytesPer3Sec,
                data.size()
            )
            data.reset()
            data.write(remaining)
        }
    }

    private fun ByteArray.setInt(position: Int, value: Int) {
        for (i in 0..3) {
            this[position + i] = (value shr (i * 8)).toByte()
        }
    }

    private fun ByteArray.setShort(position: Int, value: Short) {
        for (i in 0..1) {
            this[position + i] = (value.toInt() shr (i * 8)).toByte()
        }
    }

    private fun ByteArray.setString(position: Int, value: String) {
        value.toByteArray(Charsets.US_ASCII).copyInto(this, position, 0, 4.coerceAtMost(value.length))
    }

    interface VoiceApiService {
        @Multipart
        @POST("transcribe")
        fun uploadAudio(@Part file: MultipartBody.Part): Call<ServerResponse>
    }

    data class ServerResponse(
        val status: String,
        val result: RecognitionResult,
        val timestamp: String
    )

    data class RecognitionResult(
        val recognized: Boolean,  // Основной флаг распознавания
        val item: String?,
        val type: String?,
        val confidence: Float?,
        val raw_text: String?,
        val normalized_text: String?
    )
}
