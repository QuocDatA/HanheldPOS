package com.hanheldpos.data.api

import com.hanheldpos.data.api.helper.ApiLogger
import com.hanheldpos.data.api.helper.BearerTokenInterceptor
import com.utils.constants.Const
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService private constructor() {

    private val loggingInterceptor =
        HttpLoggingInterceptor(ApiLogger()).setLevel(
            if (Const.DEBUG_MODE)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        )

    private lateinit var bearerTokenInterceptor: BearerTokenInterceptor
    private var retrofit: Retrofit? = null

    fun init(accessToken: String? = null) {
        bearerTokenInterceptor = BearerTokenInterceptor(accessToken)
        retrofit = createBuilder()
    }

    private fun createBuilder(): Retrofit {
        val okHttpBuilder = OkHttpClient.Builder()
            .readTimeout(REQ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(REQ_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(REQ_TIME_OUT, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_1_1))
            .apply {
                addInterceptor(loggingInterceptor)
                addInterceptor(bearerTokenInterceptor)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConst.BASE_URL)
            .client(okHttpBuilder)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Create API service
     */
    fun <T> createService(serviceClazz: Class<T>): T {
        return createBuilder().create(serviceClazz)
    }

    companion object {

        @Volatile
        private var instance: RetrofitService? = null

        fun get(): RetrofitService =
            instance ?: synchronized(this) {
                val newInstance =
                    instance ?: RetrofitService().also { instance = it }
                newInstance
            }

        private const val REQ_TIME_OUT: Long = 60
    }
}