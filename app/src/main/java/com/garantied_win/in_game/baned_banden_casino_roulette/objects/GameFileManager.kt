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
        return if (fileIsInvalid(storageText)) {
            storageText = BANK_DEFAULT
            storage.writeText(storageText)
            10_000
        } else {
            Json.decodeFromString<GameDataEntity>(storageText).bank.let {
                if (it >= 10) it else 10_000
            }
        }
    }

    fun fileIsInvalid(fileText: String): Boolean {
        val check1 = fileText.isEmpty()
        val check2 = fileText.startsWith("{") && fileText.endsWith("}")
        val check3 = fileText.contains("bank") && fileText.contains("requested")
        return check1 || !check2 || !check3
    }

    fun getRequested(context: Context) : Boolean {
        val storage = File(context.filesDir, "game_storage.txt")
        if (!storage.exists()) {
            storage.createNewFile()
            storage.writeText(BANK_DEFAULT)
        }
        var storageText = storage.readText()
        return if (fileIsInvalid(storageText)) {
            storageText = BANK_DEFAULT
            storage.writeText(storageText)
            false
        } else {
            Json.decodeFromString<GameDataEntity>(storageText).requested
        }
    }

    fun setBank(context: Context, value: Int) {
        val storage = File(context.filesDir, "game_storage.txt")
        if (!storage.exists()) {
            storage.createNewFile()
        }
        var storageText = storage.readText()
        val obj = Json.decodeFromString<GameDataEntity>(if (fileIsInvalid(storageText)) {
            storageText = BANK_DEFAULT
            storage.writeText(storageText)
            storageText
        } else {
            storageText
        })
        obj.bank = value
        storage.writeText(Json.encodeToString(obj))
    }

    fun requested(context: Context) {
        val storage = File(context.filesDir, "game_storage.txt")
        if (!storage.exists()) {
            storage.createNewFile()
        }
        var storageText = storage.readText()
        val obj = Json.decodeFromString<GameDataEntity>(if (fileIsInvalid(storageText)) {
            storageText = BANK_DEFAULT
            storage.writeText(storageText)
            storageText
        } else {
            storageText
        })
        obj.requested = true
        storage.writeText(Json.encodeToString(obj))
    }

    companion object {
        private const val BANK_DEFAULT = "{\"bank\":10000,\"requested\":false}"
    }
}