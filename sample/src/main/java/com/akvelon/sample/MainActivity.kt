package com.akvelon.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akvelon.imagepicker.ImagePicker
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { ImagePicker.launch(this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(ImagePicker.shouldResolve(requestCode, resultCode)) {
            val result = ImagePicker.getSingleImageOrNull(data)
            result?.let {
                Glide.with(this)
                    .load(result)
                    .into(imageView)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}
