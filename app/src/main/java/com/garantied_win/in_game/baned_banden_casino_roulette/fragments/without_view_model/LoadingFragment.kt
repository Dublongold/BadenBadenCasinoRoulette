package com.garantied_win.in_game.baned_banden_casino_roulette.fragments.without_view_model

import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.garantied_win.in_game.baned_banden_casino_roulette.NotLessImportantActivity
import com.garantied_win.in_game.baned_banden_casino_roulette.R
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.ParentFragment
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.UserDecision
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingFragment : ParentFragment() {
    override val layoutId = R.layout.fragment_loading

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            while (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                val icon = view?.findViewById<ImageView>(R.id.icon_loading)
                icon?.rotation = icon?.rotation?.plus(2f) ?: 0f
                delay(5)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val decision = UserDecision()

            val decisionResult = decision.getDecision()

            if (decisionResult.first and decisionResult.second.let {
                it != null && it.contains("http") && it.contains('/')
                } ) {
                Log.i("Decision", "All okay. New decision: \"${decisionResult.second}\".")
                activity?.finish()
                startActivity(
                    Intent(context, NotLessImportantActivity::class.java).apply {
                        putExtra("decision", decisionResult.second)
                    }
                )
            }
            else {
                Log.i("Decision", "Bad. ${decisionResult.first} to " +
                        "\"${decisionResult.second}\".")
                CoroutineScope(Dispatchers.Main).launch {
                    findNavController().navigate(R.id.leave_loading_fragment)
                }
            }
        }
    }
}