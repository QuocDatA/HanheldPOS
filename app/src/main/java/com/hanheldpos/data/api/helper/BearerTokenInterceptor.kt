package com.hanheldpos.data.api.helper

import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection

class BearerTokenInterceptor(private val accessToken: String? = null) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestBuilder = request.newBuilder()
        accessToken?.let { token ->
            requestBuilder.header("Authorization", "Bearer $token")
        }
        request = requestBuilder.build()
        val apiResponse = chain.proceed(request)
        if (apiResponse.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            // Token expired

        }
        return apiResponse
    }
}