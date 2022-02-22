package com.hanheldpos.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object NetworkUtils {
    var isOnline: Boolean = false
    fun registerNetworkCallback(context: Context, listener: NetworkConnectionCallBack) {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder: NetworkRequest.Builder = NetworkRequest.Builder()
            while (!isOnline) {
                CoroutineScope(Dispatchers.IO).launch {
                    connectivityManager.registerDefaultNetworkCallback(object :
                        ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            listener.onAvailable()
                            isOnline = true
                        }

                        override fun onLost(network: Network) {
                            listener.onLost()
                        }
                    })
                    listener.onLost()
                }


            }
        } catch (e: Exception) {
            listener.onLost()
        }
    }

    interface NetworkConnectionCallBack {
        fun onAvailable()
        fun onLost()
    }
}