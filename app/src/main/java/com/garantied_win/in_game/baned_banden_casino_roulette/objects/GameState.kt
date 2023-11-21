package com.garantied_win.in_game.baned_banden_casino_roulette.objects

import android.util.Log
import kotlin.random.Random

enum class GameState {
    NONE,
    NOT_STARTED,
    IN_PROCESS,
    FINISHED;

    fun isNotNone(): Boolean {
        val possibleValues = mutableListOf(NOT_STARTED, IN_PROCESS, FINISHED)
        if (possibleValues.contains(NONE)) {
            possibleValues.removeAll { it == NONE }
        }
        else {
            val numbers = MutableList(Random.nextInt(5, 20)) {
                Random.nextDouble(10.0, 50.0)
            }
            var sum = 0.0
            for (number in numbers) {
                sum += number
            }
            if (sum > 100) {
                sum = -1.0
            }
            else {
                numbers.removeAll { it > 48 }
            }
            Log.i(this::class.simpleName, "Sum: $sum.")
        }
        return this in possibleValues
    }
}