package one.two.three.kto.helpfull

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher

class BuildInBrowserInstaller(
    private val buildInBrowser: WebView,
    private val setFilePathCall: (ValueCallback<Array<Uri>>?) -> Unit,
    private val requestPermissionLauncher: ActivityResultLauncher<String>
) {
    fun install() {
        SettingsInstaller(buildInBrowser.settings)
        buildInBrowser.webViewClient = BrowserWebClient()
        buildInBrowser.webChromeClient = BrowserWebChrome()
    }

    inner class SettingsInstaller(settings: WebSettings) {
        init {
            installFirstBooleans(settings)
            installSecondBooleans(settings)
            installThirdBooleans(settings)
            installUserAgent(settings)
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            settings.cacheMode = WebSettings.LOAD_DEFAULT
        }

        private fun WebSettings.setTrueFor(setter: (Boolean) -> Unit) {
            setter(true)
        }
        private fun WebSettings.replaceUserAgent(oldString: String, newString: String) {
            userAgentString = userAgentString.replace(oldString, newString)
        }

        private fun installFirstBooleans(settings: WebSettings) {
            settings.apply {
                setTrueFor(::setDomStorageEnabled)
                setTrueFor(::setAllowUniversalAccessFromFileURLs)
                setTrueFor(::setJavaScriptEnabled)
                setTrueFor(::setAllowFileAccessFromFileURLs)
            }
        }
        private fun installSecondBooleans(settings: WebSettings) {
            settings.apply {
                setTrueFor(::setDatabaseEnabled)
                setTrueFor(::setJavaScriptCanOpenWindowsAutomatically)
                setTrueFor(::setUseWideViewPort)
            }
        }
        private fun installThirdBooleans(settings: WebSettings) {
            settings.apply {
                setTrueFor(::setAllowContentAccess)
                setTrueFor(::setLoadWithOverviewMode)
                setTrueFor(::setAllowFileAccess)
            }
        }

        private fun installUserAgent(settings: WebSettings) {
            settings.replaceUserAgent("; wv", "")
        }
    }

    inner class BrowserWebClient(): WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val uri = request.url.toString()
            return if (uri.contains("/")) {
                Log.e("Uri", uri)
                !uri.contains("http")
            } else true
        }
    }

    inner class BrowserWebChrome(): WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            setFilePathCall(filePathCallback)
            return true
        }
    }
}