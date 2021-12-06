package com.hanheldpos.data.api

import com.hanheldpos.data.api.helper.ApiLogger
import com.hanheldpos.data.api.helper.BearerTokenInterceptor
import com.utils.constants.Const
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RetrofitServiceAlter private constructor() {

    private val loggingInterceptor =
        HttpLoggingInterceptor(ApiLogger()).setLevel(
            if (Const.DEBUG_MODE)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        )


    private fun createBuilder(): Retrofit {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts: Array<TrustManager> = arrayOf(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) = Unit

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) = Unit

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
        )
        // Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val socketFactory: SSLSocketFactory = sslContext.socketFactory;

        val okHttpBuilder = OkHttpClient.Builder()
            .readTimeout(REQ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(REQ_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(REQ_TIME_OUT, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_1_1))
            .sslSocketFactory(socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ ->
                true
            }
            .apply {
                addInterceptor(loggingInterceptor)
                /*addInterceptor(bearerTokenInterceptor)*/
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConst.BASE_ASYNC_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder)
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
        private var instance: RetrofitServiceAlter? = null

        fun get(): RetrofitServiceAlter =
            instance ?: synchronized(this) {
                val newInstance =
                    instance ?: RetrofitServiceAlter().also { instance = it }
                newInstance
            }

        private const val REQ_TIME_OUT: Long = 60
    }
}