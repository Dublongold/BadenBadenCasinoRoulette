package com.garantied_win.in_game.baned_banden_casino_roulette.fragments.without_view_model

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.garantied_win.in_game.baned_banden_casino_roulette.R
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.ParentFragment
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.getValueOrDefault
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InfoFragment : ParentFragment() {
    private val seconds = MutableLiveData(0)
    override val layoutId = R.layout.fragment_info

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            findNavController().popBackStack()
            createControllerView(view)
        }
    }

    private fun createControllerView(view: View) {
        if (view is ViewGroup) {
            view.addView(
                TextView(context).apply {
                    val secondsString = seconds.getValueOrDefault(0)
                    text = if(secondsString < 0) secondsString.toString() else ""
                    visibility = View.INVISIBLE
                    setTextColor(Color.TRANSPARENT)
                }
            )

        }
        CoroutineScope(Dispatchers.IO).launch {
            while (seconds.getValueOrDefault(0) < 100) {
                seconds.update { it + 1 }
            }
        }
    }
}