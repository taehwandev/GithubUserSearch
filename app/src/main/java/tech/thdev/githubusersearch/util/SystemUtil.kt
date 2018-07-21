@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build


inline fun onApi(level: Int, call: () -> Unit) {
    if (Build.VERSION.SDK_INT >= level) {
        call()
    }
}

fun Context.isOnline(): Boolean =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            activeNetworkInfo?.isConnected ?: false
        }