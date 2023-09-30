@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


inline fun Context.isOnline(): Boolean =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            activeNetworkInfo?.isConnected ?: false
        }

inline fun AppCompatActivity.loadFragment(@IdRes idRes: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().run {
        this.replace(idRes, fragment)
    }.commit()
}