package com.garantied_win.in_game.baned_banden_casino_roulette.fragments.without_view_model

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.garantied_win.in_game.baned_banden_casino_roulette.R
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.ParentFragment

class MenuFragment : ParentFragment() {
    override val layoutId = R.layout.fragment_menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.open_info_button).setOnClickListener {
            findNavController().navigate(R.id.open_info_fragment)
        }
        view.findViewById<AppCompatButton>(R.id.open_game_button).setOnClickListener {
            findNavController().navigate(R.id.open_game_fragment)
        }
    }
}