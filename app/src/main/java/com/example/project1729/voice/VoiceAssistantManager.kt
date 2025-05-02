package com.example.project1729.voice

import android.content.Context
import java.util.concurrent.atomic.AtomicBoolean

object VoiceAssistantManager {
    private var voiceAssistant: VoiceAssistant? = null
    private var currentCallback: VoiceAssistant.VoiceCallback? = null
    private var isRecording = AtomicBoolean(false)

    fun registerCallback(callback: VoiceAssistant.VoiceCallback) {
        currentCallback = callback
        voiceAssistant?.updateCallback(callback)
    }

    fun unregisterCallback() {
        currentCallback = null
    }

    fun initialize(context: Context, callback: VoiceAssistant.VoiceCallback) {
        if (voiceAssistant == null) {
            voiceAssistant = VoiceAssistant(context, callback)
        } else {
            voiceAssistant?.updateCallback(callback)
        }
        currentCallback = callback
    }

    fun start() {
        voiceAssistant?.start()
        isRecording.set(true)
    }

    fun stop() {
        voiceAssistant?.stop()
        isRecording.set(false)
    }

    fun isRecording() = isRecording.get()

    fun clear() {
        stop()
        voiceAssistant = null
        currentCallback = null
    }
}