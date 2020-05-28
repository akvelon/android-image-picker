package com.akvelon.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akvelon.imagepicker.ImagePicker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImagePicker.launch(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(ImagePicker.shouldResolve(requestCode, resultCode)) {
            val result = ImagePicker.getImages(data)
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}
