package com.garantied_win.in_game.baned_banden_casino_roulette.objects

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class GameFileManager {
    fun getBank(context: Context): Int {
        val storage = File(context.filesDir, "game_storage.txt")
        if (!storage.exists()) {
            storage.createNewFile()
            storage.writeText(BANK_DEFAULT)
        }
        var storageText = storage.readText()
        return if (storageText.isEmpty() ||
            !(storageText.startsWith("{") && storageText.endsWith("}"))
        ) {
            storageText = BANK_DEFAULT
            storage.writeText(storageText)
            10_000
        } else {
            Json.decodeFromString<GameDataEntity>(storageText).bank.let {
                if (it >= 10) it else 10_000
            }
        }
    }

    fun setBank(context: Context, value: Int) {
        val storage = File(context.filesDir, "game_storage.txt")
        if (!storage.exists()) {
            storage.createNewFile()
        }
        val gameDataEntity = GameDataEntity(value)
        storage.writeText(Json.encodeToString(gameDataEntity))
    }

    companion object {
        private const val BANK_DEFAULT = "{\"bank\":10000}"
    }
}