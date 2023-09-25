package com.brbrasileoktoo.essporte.helpfull

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class RequestHelper(
    private val getSingleUriCallback: () -> Uri?,
    private val setSingleUriCallback: (Uri?) -> Unit
) {
    var singleUriForCallback: Uri?
        get() {
            return getSingleUriCallback()
        }
        set(value) {
            setSingleUriCallback(value)
        }

    fun start(lifecycleCoroutineScope: LifecycleCoroutineScope, externalDirection: File?, doAfterCreate: (Intent) -> Unit) {
        lifecycleCoroutineScope.launch(Dispatchers.IO) {
            val image = try {
                File.createTempFile(
                    "image",
                    ".jpg",
                    externalDirection
                )
            }
            catch (e: Exception) {
                Log.e("Saving image", "Exception while saving image.", e)
                null
            }
            val takeImage = takeImageIntent(image)
            singleUriForCallback = Uri.fromFile(image)
            val old = oldIntent()
            val chooser = chooserIntent(old, arrayOf(takeImage))
            doAfterCreate(chooser)
        }
    }

    private fun takeImageIntent(image: File?): Intent {
        return Intent().apply {
            action = MediaStore.ACTION_IMAGE_CAPTURE
            putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image))
        }
    }

    private fun oldIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
    }

    private fun chooserIntent(oldIntent: Intent, intentArray: Array<Intent>): Intent {
        return Intent().apply {
            action = Intent.ACTION_CHOOSER
            putExtra(Intent.EXTRA_INTENT, oldIntent)
            putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        }
    }
}