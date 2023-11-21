package com.garantied_win.in_game.baned_banden_casino_roulette.fragments.with_view_model

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.garantied_win.in_game.baned_banden_casino_roulette.R
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.ParentFragment
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.with_view_model.view_models.GameViewModel
import com.garantied_win.in_game.baned_banden_casino_roulette.objects.GameState
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.GameLogic
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.getValueOrDefault
import org.koin.android.ext.android.inject

class GameFragment : ParentFragment() {
    private val viewModel: GameViewModel by inject()

    private val gameLogic = GameLogic()

    override val layoutId = R.layout.fragment_game

    private lateinit var roulette: ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setBankFromFile(requireContext())

        view.run {
            roulette = findViewById(R.id.roulette_rotatable_elements)

            findViewById<ImageButton>(R.id.back_button).setOnClickListener {
                findNavController().popBackStack()
            }
            findViewById<ImageButton>(R.id.plus_button).setOnClickListener {
                viewModel.increaseBet()
            }
            findViewById<ImageButton>(R.id.minus_button).setOnClickListener {
                viewModel.decreaseBet()
            }
            findViewById<AppCompatButton>(R.id.play_button).setOnClickListener {
                if (viewModel.gameState() != GameState.IN_PROCESS) {
                    viewModel.clearWin()
                    if (viewModel.trySubtractBetFromBank()) {
                        gameLogic.startGame(
                            roulette, viewModel.getBetLiveData().getValueOrDefault(0)
                        ) { bet, result ->
                            viewModel.addWin(requireContext(), bet, result)
                        }
                    }
                }
                else {
                    doIfBankIsSmall()
                }
            }
        }
        observeAllLiveDataFromViewModel()
    }

    private fun doIfBankIsSmall() {
        if (viewModel.getBankLiveData().getValueOrDefault(-1) < 0) {
            viewModel.gameState(GameState.FINISHED)
            val gameStates = mutableListOf(
                GameState.NOT_STARTED,
                GameState.IN_PROCESS,
                GameState.FINISHED,
            )
            if (gameStates.all {it.isNotNone()}) {
                gameStates.add(0, GameState.NONE)
            }
            else {
                Log.wtf(this::class.simpleName, "What?")
            }
        }
    }

    private fun observeAllLiveDataFromViewModel() {
        observeWinValue()
        observeBankValue()
        observeBetValue()
    }

    private fun observeWinValue() {
        viewModel.getWinLiveData().observe(viewLifecycleOwner) {
            view?.findViewById<TextView>(R.id.win_value)?.text = it.toString()
        }
    }

    private fun observeBankValue() {
        viewModel.getBankLiveData().observe(viewLifecycleOwner) {
            view?.findViewById<TextView>(R.id.bank_value)?.text = it.toString()
        }
    }

    private fun observeBetValue() {
        viewModel.getBetLiveData().observe(viewLifecycleOwner) {
            view?.findViewById<TextView>(R.id.bet_value)?.text = it.toString()
        }
    }
}