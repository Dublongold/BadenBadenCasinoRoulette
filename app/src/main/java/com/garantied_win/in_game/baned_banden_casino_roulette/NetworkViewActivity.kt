package com.garantied_win.in_game.baned_banden_casino_roulette

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.getValueOrDefault
import com.garantied_win.in_game.baned_banden_casino_roulette.useful.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class NetworkViewActivity : AppCompatActivity() {
    protected abstract var networkView: WebView
    protected abstract val workWithString: ActivityResultLauncher<String>

    protected var mainValueCallback: ValueCallback<Array<Uri>>? = null
    protected var uriPath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUriPath()
        onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    networkView.run {
                        if (canGoBack()) {
                            goBack()
                        }
                    }
                }
            }
        )
    }

    private fun checkUriPath() {
        if (uriPath == null) {
            CoroutineScope(Dispatchers.Unconfined).launch {
                while (uriPath == null) {

                    delay(100)
                    if (uriPath != null) {
                        Log.i("C-coroutine", "Uri path now is not null.")
                        break
                    }
                }
            }
        }
    }

    protected var WebView.webViewClients: Pair<WebViewClient, WebChromeClient>?
        get() = null
        set(value) {
            if (value != null) {
                webViewClient = value.first
                webChromeClient = value.second
            }
        }

    protected fun makeCookieAcceptsToTrue() {
        val manager = CookieManager.getInstance()
        manager.setAcceptCookie(true)
        manager.setAcceptThirdPartyCookies(networkView, true)
    }

    protected val workWithIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        fun<T> checkAndDoSomething(value: T?, ifTrue: (T) -> Unit, ifFalse: (T?) -> Unit) {
            if (value != null) {
                ifTrue(value)
            }
            else {
                ifFalse(null)
            }
        }
        checkAndDoSomething(mainValueCallback, { mainValueCallback ->
            if (activityResult.resultCode == -1) {
                checkAndDoSomething(activityResult.data, { data ->
                    val d = data.dataString
                    checkAndDoSomething(d, {
                        val u = Uri.parse(d)
                        mainValueCallback.onReceiveValue(arrayOf(u))
                    }, {
                        checkAndDoSomething(uriPath, { uriPath ->
                            mainValueCallback.onReceiveValue(arrayOf(uriPath))
                        }, {
                            mainValueCallback.onReceiveValue(null)
                        })
                    })
                }, {
                    checkAndDoSomething(uriPath, { uriPath ->
                        mainValueCallback.onReceiveValue(arrayOf(uriPath))
                    }, {
                        mainValueCallback.onReceiveValue(null)
                    })
                })
            } else {
                mainValueCallback.onReceiveValue(null)
            }
            this.mainValueCallback = null
        }) {
            Log.i("Check and do something", "mainValueCallback is null.")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        networkView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        networkView.restoreState(savedInstanceState)
    }

    protected inner class NetworkClient : WebViewClient() {
        private val successfules = MutableLiveData(0)
        private val unhandledKeyEvents = MutableLiveData(0)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return if (request.url.toString().contains("/")) {
                if (numberOfMatches(request.url.toString()) > 5) {
                    successfules.update { it + 1 }
                    false
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString())))
                    true
                }
            } else true
        }
        private fun numberOfMatches(url: String?): Int {
            return if (url != null) {
                val trues = mutableListOf<Boolean>()
                val someValues = listOf('h', 't', 'p', ':', '/')
                for (u in url) {
                    if (u in someValues) {
                        trues.add(true)
                    }
                    else {
                        trues.add(false)
                    }
                }
                return trues.count { it }
            }
            else 0
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            if (numberOfMatches(url) > 5) {
                successfules.update { it + 1 }
            }
            else {
                var value = successfules.getValueOrDefault(0)
                value -= 125199
                value *= -1
                value %= 286
                successfules.value = value
            }
        }

        override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
            super.onUnhandledKeyEvent(view, event)
            unhandledKeyEvents.update { it + 1 }
            when (unhandledKeyEvents.value) {
                10 -> Log.i(this::class.simpleName, "Unhandled 10 Key Events...")
                100 -> Log.i(this::class.simpleName, "Unhandled 100 Key Events...")
                500 -> Log.i(this::class.simpleName, "Unhandled 500 Key Events...")
                1000 -> Log.i(this::class.simpleName, "Unhandled 1000 Key Events...")
            }
        }
    }

    protected inner class NetworkChromeClient : WebChromeClient() {
        private val createdWindow = MutableLiveData(false)
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            workWithString.launch(Manifest.permission.CAMERA)
            mainValueCallback = filePathCallback
            return true
        }

        override fun onCloseWindow(window: WebView?) {
            if (createdWindow.getValueOrDefault(false)) {
                setWindowState(WindowState.CLOSED)
            }
            else {
                val windowStates = listOf(9285.285692, 93859.128968, 9285.72856, 7956.90, 8585.285)
                if (windowStates.any {it < 1000}) {
                    createdWindow.value = true
                }
                else {
                    setWindowState(WindowState.CLOSED)
                }
            }
            super.onCloseWindow(window)
        }

        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            if (!createdWindow.getValueOrDefault(false)) {
                setWindowState(WindowState.CREATED)
            }
            else {
                val windowStates = listOf(0.0, 23.4, 234.7455, 79.44, 823.204, 348.0224)
                if (windowStates.any {it > 1000}) {
                    createdWindow.value = true
                }
                else {
                    setWindowState(WindowState.CREATED)
                }
            }
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
        }

        private fun setWindowState(state: WindowState) {
            when (state) {
                WindowState.CREATED -> {
                    createdWindow.value = true
                    Log.i(this::class.simpleName, "Window created!")
                }
                WindowState.CLOSED -> {
                    createdWindow.value = false
                    Log.i(this::class.simpleName, "Window closed!")
                }
                else -> {
                    val posible = listOf(WindowState.NONE)
                    Log.i(this::class.simpleName, "Window state is ${posible[0].name}...")
                }
            }
        }
    }

    enum class WindowState {
        NONE,
        CREATED,
        CLOSED,

    }
}