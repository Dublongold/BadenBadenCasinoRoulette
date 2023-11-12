package com.garantied_win.in_game.baned_banden_casino_roulette.useful

import android.content.Context
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.garantied_win.in_game.baned_banden_casino_roulette.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameLogic {
    // roulette element id is roulette_rotatable_elements
    fun startGame(roulette: ConstraintLayout, bet: Int, resultCallback: (Int, Double) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            var haveAnotherSpin = true
            do {
                if (roulette.id == R.id.roulette_rotatable_elements) {
                    haveAnotherSpin = false

                    var rotationPower = 0f

                    repeat(Random.nextInt(500, 700)) {
                        if (rotationPower < 3f) {
                            rotationPower += 0.02f
                        }
                        roulette.rotation += rotationPower
                        delay(5)
                    }
                    while (rotationPower > 0f) {
                        rotationPower -= 0.01f
                        roulette.rotation += rotationPower
                        delay(5)
                    }
                    val result = matchRotation(roulette.rotation)
                    if (result != -1.0) {
                        resultCallback(bet, result)
                    }
                    else {
                        haveAnotherSpin = true
                    }
                } else {
                    Log.e(
                        "Game logic",
                        "Why roulette id isn't R.id.roulette_rotatable_elements?"
                    )
                }
            }
            while (haveAnotherSpin)
        }
    }

    fun matchRotation(rotation: Float) : Double {
        return when (if (rotation > 360f) rotation % 360 else rotation) {
            /* First lose */ in 0f..<14f, in 347f..<360f,
            /* Second lose */ in 46f..<74.4f,
            /* Third lose */ in 226.5f..<254f -> 0.0
            in 14f..<46f -> 4.5
            in 74.4f..<107f -> 3.2
            in 107f..<134f -> 1.3
            in 134f..<167f -> -1.0
            in 167f..<194f -> 1.9
            in 194f..<226.5f -> 1.4
            in 254f..<287f -> 10.0
            in 287f..<314f -> 1.5
            in 314f..<347f -> 8.2
            else -> 0.0
        }
    }
}