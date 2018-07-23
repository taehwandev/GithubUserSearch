package tech.thdev.githubusersearch.util

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class AutoReleaseLifecycle<T : Any>(fragment: Fragment) : ReadWriteProperty<Fragment, T> {

    protected var _value: T? = null

    init {
        fragment.lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                _value = null
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
                "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        _value = value
    }
}

/**
 * Creates an [AutoReleaseLifecycle] associated with this fragment.
 */
fun <T : Any> Fragment.autoRelease() = AutoReleaseLifecycle<T>(this)