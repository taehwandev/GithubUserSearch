@file:Suppress("UNCHECKED_CAST")

package tech.thdev.githubusersearch.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

fun <T : ViewModel> Class<T>.inject(fragment: Fragment, onCreateViewModel: () -> T): T =
        ViewModelProviders.of(fragment, createViewModel(onCreateViewModel)).get(this)

fun <T : ViewModel> Class<T>.inject(fragmentActivity: FragmentActivity, onCreateViewModel: () -> T): T {
    return ViewModelProviders.of(fragmentActivity, createViewModel(onCreateViewModel)).get(this)
}

private fun <T : ViewModel> createViewModel(onCreateViewModel: () -> T) = object : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return onCreateViewModel() as T
    }
}