package one.two.three.kto

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import one.two.three.kto.helpfull.BuildInBrowserInstaller
import one.two.three.kto.models.FirebaseDataContainer
import java.io.File
import java.io.IOException


class WebActivity : AppCompatActivity() {
    lateinit var buildInBrowser: WebView
    var filePathCall: ValueCallback<Array<Uri>>? = null
    var singleUriForCallback: Uri? = null
    private var beginnerBrowserLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        buildInBrowser = WebView(this)
        buildInBrowser.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        
        findViewById<ConstraintLayout>(R.id.activity_web).addView(buildInBrowser)

        beginnerBrowserLink = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("data", FirebaseDataContainer::class.java)?.link
        }
        else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<FirebaseDataContainer>("data")?.link
        }
        installBuildInBrowser()
        buildInBrowser.loadUrl(beginnerBrowserLink!!)
    }

    private fun installBuildInBrowser() {
        BuildInBrowserInstaller(buildInBrowser, ::filePathCall.setter, requestPermissionLauncher).install()
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(buildInBrowser, true)
    }

    val requestPermissionLauncher: ActivityResultLauncher<String> = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean? ->
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = File.createTempFile(
                "file",
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
        } catch (ex: IOException) {
            Log.e("PhotoFile", "Unable to cre", ex)
        }
        takePictureIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            Uri.fromFile(photoFile)
        )
        singleUriForCallback = Uri.fromFile(photoFile)
        val old = Intent(Intent.ACTION_GET_CONTENT)
        old.addCategory(Intent.CATEGORY_OPENABLE)
        old.type = "*/*"
        val intentArray = arrayOf(takePictureIntent)
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, old)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        startActivityForResult(chooserIntent, 1)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (filePathCall == null) return
        if (resultCode == -1) {
            if (data != null) {
                val d = data.dataString
                if (d != null) {
                    val u = Uri.parse(d)
                    filePathCall!!.onReceiveValue(arrayOf(u))
                } else {
                    if (singleUriForCallback != null) {
                        filePathCall!!.onReceiveValue(arrayOf(singleUriForCallback!!))
                    } else {
                        filePathCall!!.onReceiveValue(null)
                    }
                }
            } else {
                if (singleUriForCallback != null) {
                    filePathCall!!.onReceiveValue(arrayOf(singleUriForCallback!!))
                } else {
                    filePathCall!!.onReceiveValue(null)
                }
            }
        } else {
            filePathCall!!.onReceiveValue(null)
        }
        filePathCall = null
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        buildInBrowser.saveState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == 4 && buildInBrowser.canGoBack()) {
            buildInBrowser.goBack()
            return true
        }
        return false
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        buildInBrowser.restoreState(savedInstanceState)
    }
}