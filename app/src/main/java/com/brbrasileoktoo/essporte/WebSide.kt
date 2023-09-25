package com.brbrasileoktoo.essporte

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.brbrasileoktoo.essporte.helpfull.BuildInBrowserInstaller
import com.brbrasileoktoo.essporte.helpfull.RequestHelper
import com.brbrasileoktoo.essporte.models.FirebaseDataContainer


class WebSide : AppCompatActivity() {
    lateinit var buildInBrowser: WebView
    var filePathCall: ValueCallback<Array<Uri>>? = null
    var singleUriForCallback: Uri? = null
    private var beginnerBrowserLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_web)

        buildInBrowser = WebView(this)
        buildInBrowser.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        
        findViewById<ConstraintLayout>(R.id.activity_web).addView(buildInBrowser)

        beginnerBrowserLink = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("data", FirebaseDataContainer::class.java)?.whereLet
        }
        else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<FirebaseDataContainer>("data")?.whereLet
        }
        installBuildInBrowser()
        buildInBrowser.loadUrl(beginnerBrowserLink!!)
    }

    private fun installBuildInBrowser() {
        BuildInBrowserInstaller(buildInBrowser, ::filePathCall.setter, requestPermissionLauncher).install()
        val coocManag = CookieManager.getInstance()
        coocManag.apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(buildInBrowser, true)
            onBackPressedDispatcher.addCallback(MainOnBackPressedCallback(true))
        }
    }

    val requestPermissionLauncher: ActivityResultLauncher<String> = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) {
        val requestHelper = RequestHelper(::singleUriForCallback.getter, ::singleUriForCallback.setter)
        requestHelper.start(
            lifecycleScope,
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        ) {
            startActivityForResult(it, 1)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val onActivityResultObject = object {
            val GOOD = 0
            val ONE = 1
            val TWO = 2
            val THREE = 3

            fun doMainBranch(): Int {
                val localFilePathCall = filePathCall
                if(localFilePathCall != null) {
                    if(resultCode == -1) {
                        return if(data != null) {
                            val str = data.dataString
                            if(str != null) {
                                val uri = Uri.parse(str)
                                localFilePathCall.onReceiveValue(arrayOf(uri))
                                GOOD
                            } else {
                                THREE
                            }
                        } else {
                            THREE
                        }
                    }
                    else {
                        return TWO
                    }
                }
                else {
                    return ONE
                }
            }

            fun start() {
                when(doMainBranch()) {
                    GOOD -> {
                        filePathCall = null
                        Log.i("File path callback", "All right!")
                    }
                    THREE -> {
                        filePathCall?.apply {
                            onReceiveValue(if(singleUriForCallback != null)  arrayOf(singleUriForCallback!!) else null)
                            filePathCall = null
                        }
                        Log.i("File path callback", "Data or dataString is null.")
                    }
                    TWO -> {
                        filePathCall?.onReceiveValue(null)
                        Log.i("File path callback", "Result code != 1.")
                    }
                    ONE -> {
                        Log.i("File path callback", "Null.")
                    }
                }
            }
        }
        onActivityResultObject.start()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        doSomethingWithState(true, outState)
    }

    inner class MainOnBackPressedCallback(enabled: Boolean): OnBackPressedCallback(enabled) {
        override fun handleOnBackPressed() {
            if(buildInBrowser.canGoBack()) {
                buildInBrowser.goBack()
            }
        }
    }

    private fun doSomethingWithState(isSave: Boolean, state: Bundle) {
        if(isSave) {
            buildInBrowser.saveState(state)
        }
        else {
            buildInBrowser.restoreState(state)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        doSomethingWithState(false, savedInstanceState)

    }
}