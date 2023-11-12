package com.garantied_win.in_game.baned_banden_casino_roulette.objects

import androidx.lifecycle.MutableLiveData

class GameDataManager {
    private val bank = MutableLiveData(0)

    fun setStartBank(value: Int) {
        bank.value = value
    }
}