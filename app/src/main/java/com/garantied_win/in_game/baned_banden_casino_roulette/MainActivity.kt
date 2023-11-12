package com.garantied_win.in_game.baned_banden_casino_roulette

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.garantied_win.in_game.baned_banden_casino_roulette.fragments.with_view_model.view_models.GameViewModel
import com.garantied_win.in_game.baned_banden_casino_roulette.objects.GameFileManager
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                    }
                )
            }
        }
        if (!OneSignal.isInitialized) {
            OneSignal.initWithContext(this) // TODO Here must be OneSignal ID.
            CoroutineScope(Dispatchers.Main).launch {
                OneSignal.Notifications.requestPermission(true)
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