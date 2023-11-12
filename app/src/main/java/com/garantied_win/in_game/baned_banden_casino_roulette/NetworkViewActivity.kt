package com.garantied_win.in_game.baned_banden_casino_roulette

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
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

abstract class NetworkViewActivity : AppCompatActivity() {
    protected abstract var networkView: WebView
    protected abstract val workWithString: ActivityResultLauncher<String>

    protected var mainValueCallback: ValueCallback<Array<Uri>>? = null
    protected var uriPath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
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
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return if (request.url.toString().contains("/")) {
                val trues = mutableListOf<Boolean>()
                val someValues = listOf('h', 't', 'p', ':', '/')
                for (u in request.url.toString()) {
                    if (u in someValues) {
                        trues.add(true)
                    }
                    else {
                        trues.add(false)
                    }
                }
                if (trues.count { it } > 5) {
                    false
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString())))
                    true
                }
            } else true
        }
    }

    protected inner class NetworkChromeClient : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            workWithString.launch(Manifest.permission.CAMERA)
            mainValueCallback = filePathCallback
            return true
        }
    }
}