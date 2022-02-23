package com.hanheldpos.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {
    var onlineStatus = false

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }

    fun hasActiveInternetConnection(context: Context, listener: NetworkConnectionCallBack) {
        CoroutineScope(Dispatchers.IO).launch {
            while (!onlineStatus) {
                if (isOnline(context)) {
                    try {
                        val urlConnection: HttpURLConnection =
                            URL("http://www.google.com").openConnection() as HttpURLConnection
                        urlConnection.setRequestProperty("User-Agent", "Test")
                        urlConnection.setRequestProperty("Connection", "close")
                        urlConnection.setConnectTimeout(1500)
                        urlConnection.connect()
                        if (urlConnection.getResponseCode() == 200) {
                            listener.onAvailable()
                            onlineStatus = true
                        }
                    } catch (e: IOException) {
                        listener.onLost()
                    }
                } else {
                    listener.onLost()
                }
                delay(30000)
            }
        }
    }

    interface NetworkConnectionCallBack {
        fun onAvailable()
        fun onLost()
    }
}