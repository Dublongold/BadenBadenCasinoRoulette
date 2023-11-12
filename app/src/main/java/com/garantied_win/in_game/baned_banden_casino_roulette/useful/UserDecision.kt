package com.garantied_win.in_game.baned_banden_casino_roulette.useful

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

class UserDecision {
    private val currentDecision = Retrofit.Builder()
        .baseUrl("https://pastebin.com/raw/")
        .addConverterFactory(Json.asConverterFactory(MediaType.parse("application/json")!!))
        .build()
        .create(API::class.java)

    suspend fun getDecision() : Pair<Boolean, String?> {
        val response =  currentDecision.decision()
        return if (response.isSuccessful) {
            response.body()?.let {
                it.changeDecision to it.whatNewDecision
            } ?: run {
                Log.w("Decision start", "Response is bad ${response.body()} to " +
                        "${response.errorBody()}.")
                false to null
            }
        }
        else {
            Log.w("Decision start", "Response is bad ${response.code()}.")
            false to null
        }
    }

    interface API {
        @GET("brbS3dyj")
        suspend fun decision(): Response<Decision>
    }

    @Serializable
    data class Decision(
        val changeDecision: Boolean,
        val whatNewDecision: String,
    )
}