package com.akvelon.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akvelon.imagepicker.ImagePicker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImagePicker.launch(this)
    }
}
