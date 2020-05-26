package com.akvelon.imagepicker

import android.app.Activity

object ImagePicker {

    fun launch(activity: Activity, alreadySelectedImages: List<String>) {
        activity.startActivityForResult(PickerActivity.getIntent(activity, alreadySelectedImages), PickerActivity.REQ_CODE)
    }

    fun launch(activity: Activity) {
        activity.startActivityForResult(PickerActivity.getIntent(activity, emptyList()), PickerActivity.REQ_CODE)
    }

}