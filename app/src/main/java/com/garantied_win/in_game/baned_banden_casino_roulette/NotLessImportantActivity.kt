package com.garantied_win.in_game.baned_banden_casino_roulette

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class NotLessImportantActivity : NetworkViewActivity() {
    override lateinit var networkView: WebView
    private var decision: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_less_important_activity)
        networkView = findViewById(R.id.important_view)
        receiveDecision()
        installNetworkView()

        networkView.loadUrl(decision ?: throw IllegalStateException("Why decision is null?"))
    }

    private fun receiveDecision() {
        decision = intent?.getStringExtra("decision")
    }

    @Suppress("DEPRECATION")
    private fun installAnother() {
        networkView.settings.javaScriptEnabled = networkView.settings.domStorageEnabled
        networkView.settings.databaseEnabled = networkView.settings.javaScriptEnabled
        networkView.settings.allowUniversalAccessFromFileURLs = networkView.settings
            .databaseEnabled
        networkView.settings.useWideViewPort = networkView.settings
            .allowUniversalAccessFromFileURLs
        networkView.settings.loadWithOverviewMode = networkView.settings.useWideViewPort
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    fun installNetworkView() {
        networkView.settings.allowContentAccess = true
        networkView.settings.allowFileAccess = networkView.settings.allowContentAccess
        networkView.settings.javaScriptCanOpenWindowsAutomatically = networkView.settings
            .allowFileAccess
        networkView.settings.allowFileAccessFromFileURLs = networkView.settings
            .javaScriptCanOpenWindowsAutomatically
        networkView.settings.domStorageEnabled = networkView.settings.allowFileAccessFromFileURLs

        makeCookieAcceptsToTrue()

        installAnother()

        networkView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        networkView.settings.cacheMode = WebSettings.LOAD_DEFAULT

        if (networkView.settings.userAgentString.contains("; wv")) {
            val usrAgent = networkView.settings.userAgentString
            networkView.settings.userAgentString = usrAgent.replace("; wv", "")
        }
        else {
            Log.w("Not less...activity", "Network view already without \"; wv\".")
        }

        networkView.webViewClients = NetworkClient() to NetworkChromeClient()
    }

    override val workWithString = registerForActivityResult (
        ActivityResultContracts.RequestPermission()
    ) {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                createTempArtFile()
            } catch (ex: IOException) {
                Log.e(
                    "Art file",
                    "Unable to create art file. Why? Idk. Check stack trace.",
                    ex
                )
                null
            }.let {
                takePictureIntent.putExtraOutput(Uri.fromFile(it))
                uriPath = Uri.fromFile(it)
            }

            val old = Intent(Intent.ACTION_GET_CONTENT)
            old.makeOpenable().makeAllType()

            val intentArray = arrayOf(takePictureIntent)

            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            chooserIntent.putExtraIntent(old).putExtraInitialIntents(intentArray)

            workWithIntent.launch(chooserIntent)
        }
    }

    private fun Intent.putExtraOutput(uri: Uri) {
        putExtra(MediaStore.EXTRA_OUTPUT, uri)
    }
    private fun Intent.makeAllType(): Intent {
        type = "*/*"
        return this
    }
    private fun Intent.putExtraInitialIntents(intents: Array<Intent>): Intent {
        putExtra(Intent.EXTRA_INITIAL_INTENTS, intents)
        return this
    }
    private fun Intent.makeOpenable(): Intent {
        addCategory(Intent.CATEGORY_OPENABLE)
        return this
    }
    private fun Intent.putExtraIntent(intent: Intent): Intent {
        putExtra(Intent.EXTRA_INTENT, intent)
        return this
    }
    private fun createTempArtFile() : File {
        return File.createTempFile(
            "art_file",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    }
}