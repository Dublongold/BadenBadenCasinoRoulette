package com.garantied_win.in_game.baned_banden_casino_roulette.useful

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun<T> MutableLiveData<T>.update(callback: (T) -> T) {
    val tempValue = value
    if (tempValue != null) {
        value = callback(tempValue)
    }
    else {
        Log.w("Update live data", "Value is null. Nothing to change.")
    }
}

fun<T> LiveData<T>.getValueOrDefault(default: T) : T {
    return value ?: default
}