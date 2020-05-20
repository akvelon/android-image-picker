package com.akvelon.imagepicker

import android.content.Context

object ImagePicker {

    fun launch(context: Context, alreadySelectedImages: List<String>) {
        context.startActivity(PickerActivity.getIntent(context, alreadySelectedImages))
    }

    fun launch(context: Context) {
        context.startActivity(PickerActivity.getIntent(context, emptyList()))
    }

}