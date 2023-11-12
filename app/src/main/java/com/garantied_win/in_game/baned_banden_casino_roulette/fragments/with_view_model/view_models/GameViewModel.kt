package com.garantied_win.in_game.baned_banden_casino_roulette.fragments.with_view_model.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.garantied_win.in_game.baned_banden_casino_roulette.objects.GameFileManager
import com.garantied_win.in_game.baned_banden_casino_roulette.objects.GameState
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.getValueOrDefault
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.update
import org.koin.java.KoinJavaComponent.inject

class GameViewModel : ViewModel() {
    private val gameFileManager: GameFileManager by inject(GameFileManager::class.java)

    private val notStaticWin = MutableLiveData(0)
    private val notStaticBet = MutableLiveData(10)
    private val notStaticBank = MutableLiveData(0)

    private val notStaticGameState = MutableLiveData(GameState.NOT_STARTED)

    fun setBankFromFile(context: Context) {
        notStaticBank.value = gameFileManager.getBank(context)
    }

    fun getWinLiveData(): LiveData<Int> = notStaticWin
    fun getBetLiveData(): LiveData<Int> = notStaticBet
    fun getBankLiveData(): LiveData<Int> = notStaticBank

    fun gameState() = notStaticGameState.getValueOrDefault(GameState.NOT_STARTED)

    fun decreaseBet() {
        notStaticBet.update {
            if (it > 10) {
                it - 10
            }
            else {
                it
            }
        }
    }

    fun increaseBet() {
        notStaticBet.update {
            it + 10
        }
    }

    fun setWin(value: Int) {
        notStaticWin.value = value
    }

    fun clearWin() {
        notStaticWin.value = 0
    }

    fun addWin(context: Context, bet: Int, result: Double) {
        val win = (bet * result).toInt()
        notStaticWin.value = win
        if(win > 0) {
            gameFileManager.setBank(
                context,
                notStaticBank.getValueOrDefault(0) + win
            )
            notStaticBank.update {
                it + win
            }
        }
    }

    fun trySubtractBetFromBank(): Boolean {
        val betValue = notStaticBet.getValueOrDefault(0)

        return if (notStaticBank.getValueOrDefault(0) >= betValue) {
            notStaticBank.update {
                it - betValue
            }
            true
        }
        else false
    }
}