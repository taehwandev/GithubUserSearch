@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity


inline fun onApi(level: Int, call: () -> Unit) {
    if (Build.VERSION.SDK_INT >= level) {
        call()
    }
}

inline fun Context.isOnline(): Boolean =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            activeNetworkInfo?.isConnected ?: false
        }

inline fun AppCompatActivity.loadFragment(@IdRes idRes: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().run {
        this.replace(idRes, fragment)
    }.commit()
}