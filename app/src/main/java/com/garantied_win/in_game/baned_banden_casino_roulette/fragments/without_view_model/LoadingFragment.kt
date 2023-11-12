package com.garantied_win.in_game.baned_banden_casino_roulette.fragments.without_view_model

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.garantied_win.in_game.baned_banden_casino_roulette.NotLessImportantActivity
import com.garantied_win.in_game.baned_banden_casino_roulette.R
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.ParentFragment
import com.garantied_win.in_game.baned_banden_casino_roulette.objects.GameFileManager
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.UserDecision
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoadingFragment : ParentFragment() {
    private val gameFileManager: GameFileManager by inject()
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
        if (!gameFileManager.getRequested(requireContext())) {
            val newerMindListener =
                DialogInterface.OnClickListener { _, _ ->
                    val newerMindListener = DialogInterface.OnClickListener { _, _ ->
                        gameFileManager.requested(requireContext())
                        CoroutineScope(Dispatchers.IO).launch {
                            doAfterActions()
                        }
                    }
                    AlertDialog.Builder(requireContext())
                        .setPositiveButton("Allow", newerMindListener)
                        .setNegativeButton("Deny", newerMindListener)
                        .setMessage("The app requests permission to push notification.")
                        .create()
                        .show()

                }
            AlertDialog.Builder(requireContext())
                .setPositiveButton("Allow", newerMindListener)
                .setNegativeButton("Deny", newerMindListener)
                .setMessage("The app requests permission to collect data for analytics.")
                .create()
                .show()

        }
        else {
            CoroutineScope(Dispatchers.IO).launch {
                doAfterActions()
            }
        }
    }
    private suspend fun doAfterActions() {
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