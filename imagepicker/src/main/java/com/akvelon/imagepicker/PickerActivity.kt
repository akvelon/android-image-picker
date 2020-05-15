package com.akvelon.imagepicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class PickerActivity: AppCompatActivity() {

    private val enabledDrawable by lazy { ContextCompat.getDrawable(this, R.drawable.mult_select_bg_enabled) }
    private val disabledDrawable by lazy { ContextCompat.getDrawable(this, R.drawable.mult_select_bg_disabled) }

    private var multipleSelectEnabled = true
    private val alreadySelected by lazy { intent.extras?.getStringArrayList(KEY_LIST) }
    private val allPhotos = mutableListOf<com.akvelon.imagepicker.ImageWrapModel>()
    private var imageFile: File? = null
    private val selectedPhotos = mutableListOf<String>()
    private var selectedPosition = 0


    private fun confirmWithResult() {
        if(multipleSelectEnabled) {
            finishWithResult(ArrayList(selectedPhotos))
        } else {
            finishWithResult(arrayListOf(allPhotos[selectedPosition].id))
        }
    }

    private fun finishWithResult(images: ArrayList<String>) {
        this.setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(KEY_IMAGES_RESULT, images)
            }
        )
        finish()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "photo_${Date().time}",
            ".jpg",
            storageDir
        ).apply { imageFile = this }
    }

    private fun showCamera() {
        createImageFile()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            Uri.fromFile(imageFile!!)
        )
        startActivityForResult(camIntent,
            CAMERA_CODE
        )
    }

    companion object {
        private const val KEY_LIST = "images"
        const val KEY_IMAGES_RESULT = "images_result"
        const val REQ_CODE = 1987
        const val CAMERA_CODE = 1988
        fun getIntent(context: Context, alreadySelected: ArrayList<String>): Intent {
            return Intent(context, PickerActivity::class.java).apply {
                putExtra(KEY_LIST, alreadySelected)
            }
        }
    }
}