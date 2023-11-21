package com.garantied_win.in_game.baned_banden_casino_roulette.fragments

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.allViews
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.garantied_win.in_game.baned_banden_casino_roulette.R

abstract class ParentFragment : Fragment() {
    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (layoutId in listOf(R.layout.fragment_loading, R.layout.fragment_info, R.layout.fragment_game, R.layout.fragment_menu)) {
            return inflater.inflate(layoutId, container, false)
        }
        else {
            throw IllegalArgumentException("Why layoutId is ${layoutId}? " +
                    "It must be ${R.layout.fragment_loading}, ${R.layout.fragment_info}, " +
                    "${R.layout.fragment_game} or ${R.layout.fragment_menu}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.allViews.filter(::checkTag).map {
            it as? TextView
        }.forEach {
            if(it != null) {
                it.paint.shader = LinearGradient(
                    /*x0*/0f,
                    /*y0*/0f ,
                    /*x1*/0f,
                    /*y1*/it.textSize,
                    /*color0*/ResourcesCompat.getColor(resources, R.color.gold_start, null),
                    /*color1*/ResourcesCompat.getColor(resources, R.color.gold_end, null),
                    /*tile*/Shader.TileMode.CLAMP
                )
            }
        }
    }

    private fun checkTag(view: View): Boolean {
        val goldTextTag = getString(R.string.gold_text_tag)
        additionalCheck(view)
        return "${view.tag}".contains(goldTextTag)
    }

    private fun additionalCheck(view: View): Boolean {
        val checkList = listOf(view.tag != null, view.isEnabled, view.isVisible)
        return if (checkList.all { it }) {
            checkList[0]
        }
        else {
            true
        }
    }
}