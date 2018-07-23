@file:Suppress("UNCHECKED_CAST")

package tech.thdev.githubusersearch.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

fun <T : ViewModel> Class<T>.inject(fragment: Fragment, customKey: String = "", onCreateViewModel: () -> T): T =
        ViewModelProviders.of(fragment, createViewModel(onCreateViewModel)).run {
            if (customKey.isNotEmpty()) {
                this.get(customKey, this@inject)
            } else {
                this.get(this@inject)
            }
        }

fun <T : ViewModel> Class<T>.inject(fragmentActivity: FragmentActivity, customKey: String = "", onCreateViewModel: () -> T): T =
        ViewModelProviders.of(fragmentActivity, createViewModel(onCreateViewModel)).run {
            if (customKey.isNotEmpty()) {
                this.get(customKey, this@inject)
            } else {
                get(this@inject)
            }
        }

private fun <T : ViewModel> createViewModel(onCreateViewModel: () -> T) = object : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return onCreateViewModel() as T
    }
}