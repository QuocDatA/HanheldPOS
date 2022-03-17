package com.hanheldpos.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.hanheldpos.PosApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {
    private var isCheckOnlineStatus: Boolean = false

    private fun isActiveInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }

    private fun hasActiveInternet(): Boolean {
        return try {
            val urlConnection: HttpURLConnection =
                URL("http://www.google.com").openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("User-Agent", "Test")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.connectTimeout = 1500
            urlConnection.connect()
            true
        } catch (e: IOException) {
            false
        }
    }

    private fun isOnline(): Boolean {
        return if (isActiveInternet(PosApp.instance.applicationContext)) {
            hasActiveInternet();
        } else {
            false
        }
    }

    fun checkActiveInternetConnection(listener: NetworkConnectionCallBack) {
        CoroutineScope(Dispatchers.IO).launch {
            isCheckOnlineStatus = true
            while (isCheckOnlineStatus) {
                if (isOnline())
                    listener.onAvailable()
                else
                    listener.onLost()
                delay(30000)
            }
        }
    }

    fun cancelNetworkCheck() {
        isCheckOnlineStatus = false
    }

    interface NetworkConnectionCallBack {
        fun onAvailable()
        fun onLost()
    }
}