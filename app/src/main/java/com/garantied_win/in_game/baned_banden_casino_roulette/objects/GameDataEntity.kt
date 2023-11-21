package com.garantied_win.in_game.baned_banden_casino_roulette.objects

import kotlinx.serialization.Serializable

@Serializable
data class GameDataEntity (
    var bank: Int,
    private var requested: Boolean
) {
    fun getBank() = if (bank > 10) bank else 10_000
    fun setBank(value: Int) {
        bank = if (value >= 0) {
            value
        } else {
            bank
        }
    }
    fun isRequested() = requested

    fun makeRequested() {
        requested = true
    }
}