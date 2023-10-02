@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import android.content.Context
import android.net.ConnectivityManager


inline fun Context.isOnline(): Boolean =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            activeNetworkInfo?.isConnected ?: false
        }