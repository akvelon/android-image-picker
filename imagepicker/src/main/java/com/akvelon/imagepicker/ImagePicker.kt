package com.akvelon.imagepicker

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import java.io.File

object ImagePicker {

    fun launchWithPreselectedImages(activity: Activity, alreadySelectedImages: List<File>) {
        activity.startActivityForResult(
            PickerActivity.getIntent(
                activity,
                alreadySelectedImages.map { it.absolutePath }
            ),
            PickerActivity.REQ_CODE)
    }

    fun launchWithPreselectedImages(fragment: Fragment, alreadySelectedImages: List<File>) {
        fragment.startActivityForResult(
            PickerActivity.getIntent(
                fragment.requireActivity(),
                alreadySelectedImages.map { it.absolutePath }
            ),
            PickerActivity.REQ_CODE)
    }

    fun launch(activity: Activity) {
        activity.startActivityForResult(PickerActivity.getIntent(activity, emptyList()), PickerActivity.REQ_CODE)
    }

    fun launch(fragment: Fragment) {
        fragment.startActivityForResult(PickerActivity.getIntent(fragment.requireActivity(), emptyList()), PickerActivity.REQ_CODE)
    }

    fun shouldResolve(requestCode: Int, resultCode: Int): Boolean {
        return resultCode == Activity.RESULT_OK && requestCode == PickerActivity.REQ_CODE
    }

    fun getImages(data: Intent?): List<File> {
        return if(data == null || !data.hasExtra(PickerActivity.KEY_IMAGES_RESULT)) emptyList()
        else data.getStringArrayListExtra(PickerActivity.KEY_IMAGES_RESULT).map { File(it) }
    }

    fun getSingleImageOrNull(data: Intent?): File? {
        return if(data == null || !data.hasExtra(PickerActivity.KEY_IMAGES_RESULT)) null
        else if(data.getStringArrayListExtra(PickerActivity.KEY_IMAGES_RESULT).isEmpty()) null
        else data.getStringArrayListExtra(PickerActivity.KEY_IMAGES_RESULT).map { File(it) }[0]
    }
}