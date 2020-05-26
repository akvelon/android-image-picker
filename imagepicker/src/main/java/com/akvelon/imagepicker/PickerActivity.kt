package com.akvelon.imagepicker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import kotlinx.android.synthetic.main.activity_image_picker.*
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class PickerActivity: AppCompatActivity(), ImageDelegate.ClickListener {

    private val enabledDrawable by lazy { ContextCompat.getDrawable(this, R.drawable.mult_select_bg_enabled) }
    private val disabledDrawable by lazy { ContextCompat.getDrawable(this, R.drawable.mult_select_bg_disabled) }

    private var multipleSelectEnabled = true
    private val alreadySelected by lazy { intent.extras?.getStringArrayList(KEY_LIST) }
    private val allPhotos = mutableListOf<ImageWrapModel>()
    private var imageFile: File? = null
    private val selectedPhotos = mutableListOf<String>()
    private var selectedPosition = 0

    private val adapterManager by lazy {
        AdapterDelegatesManager<List<*>>()
            .apply {
                addDelegate(
                    ImageDelegate(this@PickerActivity, this@PickerActivity)
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)
        showLoading()
        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    GET_STORAGE_REQ_CODE)
            } else refresh()
        } else refresh()

        view_multipleSelect.setOnClickListener { multipleSelectChange() }
        imageView_back.setOnClickListener { finish() }
        textView_next.setOnClickListener { confirmWithResult() }
        textView_photo.setOnClickListener { showCamera() }
        (recyclerView_photos.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            GET_STORAGE_REQ_CODE -> {
                if(
                    grantResults.isNotEmpty() &&
                    permissions.isNotEmpty() &&
                    permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    permissions[1] == Manifest.permission.WRITE_EXTERNAL_STORAGE &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    refresh()
                } else finish()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun refresh() {
        showLoading()
        allPhotos.clear()
        allPhotos.addAll(mapIds(retrieveFiles()))
        showData(allPhotos)
    }

    private fun multipleSelectChange() {
        multipleSelectEnabled = !multipleSelectEnabled
        view_multipleSelect.background = if(multipleSelectEnabled) enabledDrawable else disabledDrawable
        notifyView()
    }

    private fun notifyView() {
        for(item in allPhotos) {
            item.isMultipleSelectEnabled = multipleSelectEnabled
        }
        recyclerView_photos.adapter?.notifyDataSetChanged()
    }

    private fun mapIds(ids: List<String>): List<ImageWrapModel> {
        val images = mutableListOf<ImageWrapModel>()
        var count = 0
        val preselectedList = alreadySelected ?: emptyList<String>()
        for(id in ids) {
            val isPreselected = preselectedList.contains(id)
            val currentCount = if(isPreselected) {
                count++
                count
            } else 0
            images.add(
                ImageWrapModel(
                    id,
                    isPreselected,
                    multipleSelectEnabled,
                    currentCount
                )
            )
        }
        alreadySelected?.let {
            selectedPhotos.clear()
            selectedPhotos.addAll(it)
            it.clear()
        }
        return images
    }

    private fun retrieveFiles(): MutableList<String> {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val sortType = MediaStore.MediaColumns.DATE_ADDED + " DESC"
        val cursor = this.contentResolver.query(uri, projection, null, null, sortType) ?: return mutableListOf()
        val listOfAllImages = ArrayList<String>()

        val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            listOfAllImages.add(cursor.getString(columnIndexData))
        }
        cursor.close()
        return listOfAllImages
    }

    private fun showLoading() {
        progressbar.visibility = View.VISIBLE
        imageView_photo.visibility = View.GONE
        view_expand.visibility = View.GONE
        view_multipleSelect.visibility = View.GONE
        recyclerView_photos.visibility = View.GONE
    }

    private fun showData(data: List<ImageWrapModel>) {
        progressbar.visibility = View.GONE
        imageView_photo.visibility = View.VISIBLE
        view_expand.visibility = View.GONE
        view_multipleSelect.visibility = View.VISIBLE
        recyclerView_photos.visibility = View.VISIBLE
        val image = data[0]
        image.isCurrentlySelected = true
        showImage(image.id)
        setupView(data)
    }

    private fun setupView(photos: List<ImageWrapModel>) {
        recyclerView_photos.adapter = SimpleAdapter(photos, adapterManager)
        recyclerView_photos.layoutManager = layoutManager
    }

    private fun showImage(id: String) {
        Glide.with(this)
            .load(File(id))
            .into(imageView_photo)
    }


    private val layoutManager by lazy {
        GridLayoutManager(
            this,
            4,
            RecyclerView.VERTICAL,
            false
        )
    }


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

    override fun onClick(position: Int) {
        val id = allPhotos[position].id
        showImage(id)

        if(multipleSelectEnabled) {
            onMultipleSelectClick(position)
        } else {
            if(selectedPosition == position) return

            allPhotos[position].isCurrentlySelected = true
            recyclerView_photos.adapter?.notifyItemChanged(position)

            allPhotos[selectedPosition].isCurrentlySelected = false
            recyclerView_photos.adapter?.notifyItemChanged(selectedPosition)
            selectedPosition = position
        }
    }

    override fun onMultipleSelectClick(position: Int) {
        val photo = allPhotos[position]

        if(photo.countNumber == 0) {
            if(selectedPhotos.size >= 9) return

            selectedPhotos.add(photo.id)
            photo.countNumber = selectedPhotos.size
            recyclerView_photos.adapter?.notifyItemChanged(position)
        } else {
            selectedPhotos.remove(photo.id)
            photo.countNumber = 0
            recyclerView_photos.adapter?.notifyItemChanged(position)
            for(i in 0 until selectedPhotos.size) {
                val id = selectedPhotos[i]
                for(k in 0 until allPhotos.size) {
                    val item = allPhotos[k]
                    if(item.id == id) {
                        item.countNumber = i + 1
                        recyclerView_photos.adapter?.notifyItemChanged(k)
                        break
                    }
                }
            }
        }
        if(selectedPosition == position) return

        allPhotos[position].isCurrentlySelected = true
        recyclerView_photos.adapter?.notifyItemChanged(position)
        allPhotos[selectedPosition].isCurrentlySelected = false
        recyclerView_photos.adapter?.notifyItemChanged(selectedPosition)
        selectedPosition = position
    }

    companion object {
        private const val KEY_LIST = "images"
        const val KEY_IMAGES_RESULT = "images_result"
        const val REQ_CODE = 1987
        const val CAMERA_CODE = 1988
        private const val GET_STORAGE_REQ_CODE = 1999
        fun getIntent(context: Context, alreadySelected: List<String>): Intent {
            return Intent(context, PickerActivity::class.java).apply {
                putExtra(KEY_LIST, ArrayList(alreadySelected))
            }
        }
    }
}