package com.garantied_win.in_game.baned_banden_casino_roulette.fragments.without_view_model

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.garantied_win.in_game.baned_banden_casino_roulette.R
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.ParentFragment

class InfoFragment : ParentFragment() {
    override val layoutId = R.layout.fragment_info

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}