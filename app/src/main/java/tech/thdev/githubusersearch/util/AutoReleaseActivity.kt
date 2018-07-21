package tech.thdev.githubusersearch.util

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class AutoReleaseActivity<T : Any>(activity: AppCompatActivity) : ReadWriteProperty<AppCompatActivity, T> {

    protected var _value: T? = null

    init {
        activity.lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                _value = null
            }
        })
    }

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
                "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: AppCompatActivity, property: KProperty<*>, value: T) {
        _value = value
    }
}

/**
 * Creates an [AutoReleaseActivity] associated with this activity.
 */
fun <T : Any> AppCompatActivity.autoRelase() = AutoReleaseActivity<T>(this)