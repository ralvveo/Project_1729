package com.example.project1729.di

import androidx.room.Room
import com.example.project1729.App.Companion.MAIN_URL_3
import com.example.project1729.data.db.Database
import com.example.project1729.data.network.ServerApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module{

//    single<SheetsSearchApi> {
//        val gsonBuilder = GsonBuilder()
//        gsonBuilder.setLenient()
//        val gson = gsonBuilder.create()
//        Retrofit.Builder()
//            .baseUrl(MAIN_URL_3)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//            .create(SheetsSearchApi::class.java)
//    }

    single {
        Room.databaseBuilder(
            get(), // androidContext
            Database::class.java,
            "database"
        ).build()
    }
    single<ServerApi> {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        // TrustManager, который доверяет всем сертификатам
        val trustAllCerts = arrayOf<javax.net.ssl.X509TrustManager>(
            object : javax.net.ssl.X509TrustManager {
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
            }
        )

        val sslContext = javax.net.ssl.SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0])
            .hostnameVerifier { _, _ -> true }
            .build()

        Retrofit.Builder()
            .baseUrl(MAIN_URL_3) // тот же самый URL
            .client(okHttpClient) // добавили только клиент
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ServerApi::class.java)
    }

}

