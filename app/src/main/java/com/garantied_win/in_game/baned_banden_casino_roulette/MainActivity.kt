package com.garantied_win.in_game.baned_banden_casino_roulette

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.with_view_model.view_models.GameViewModel
import com.garantied_win.in_game.baned_banden_casino_roulette.objects.GameFileManager
import com.garantied_win.in_game.baned_banden_casino_roulette.objects.GameWinContainer
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        if(GlobalContext.getKoinApplicationOrNull() == null) {
            startKoin {
                modules(
                    module {
                        viewModelOf<GameViewModel>({ GameViewModel() })
                        single {
                            gameFileManager
                        }
                        single {
                            calculateData(339)
                            GameWinContainer()
                        }
                    }
                )
            }
        }
        calculateData(12031)
        if (!OneSignal.isInitialized) {
//            OneSignal.initWithContext(this) // TODO Here must be OneSignal ID.
//            CoroutineScope(Dispatchers.Main).launch {
//                OneSignal.Notifications.requestPermission(true)
//            }
        }
    }

    private fun calculateData(data: Int) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            var jksnt = mutableListOf(data, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
            while (jksnt.any { it != jksnt[0] }) {
                if (jksnt.size >= 90) {
                    jksnt = jksnt.drop(50).toMutableList()
                }
                jksnt.add(jksnt.random())
                delay(221)
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
        else {
            var udfn = 0
            repeat (5) {
                udfn += 1
            }
        }
    }
}