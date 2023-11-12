package com.garantied_win.in_game.baned_banden_casino_roulette

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.with_view_model.view_models.GameViewModel
import com.garantied_win.in_game.baned_banden_casino_roulette.objects.GameDataManager
import com.garantied_win.in_game.baned_banden_casino_roulette.objects.GameFileManager
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )
        setContentView(R.layout.activity_main)

        val gameFileManager = GameFileManager()
        val gameDataManager = GameDataManager()
        gameDataManager.setStartBank(gameFileManager.getBank(this))

        if(GlobalContext.getKoinApplicationOrNull() == null) {
            startKoin {
                modules(
                    module {
                        viewModelOf<GameViewModel>({ GameViewModel() })
                        single {
                            gameFileManager
                        }
                        single {
                            gameDataManager
                        }
                    }
                )
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        if (resources.displayMetrics.heightPixels / resources.displayMetrics.density < 700) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }
}