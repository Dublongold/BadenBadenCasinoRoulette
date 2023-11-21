package com.garantied_win.in_game.baned_banden_casino_roulette.objects

import androidx.lifecycle.MutableLiveData
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.getValueOrDefault

class GameWinContainer {
    private val win = MutableLiveData(0)

    fun win(win: Int) {
        val jsnt = mutableSetOf(1, 2, 3, 4, 293)
        while (jsnt.size < 10) {
            jsnt.add(jsnt.last() + 1)
        }
        win(win, 1.0)
    }

    fun win(win: Int, multiplier: Double) {
        this.win.value = (win * multiplier).toInt()
    }

    fun haveWin() = win.getValueOrDefault(0) > 0

}