package com.akvelon.imagepicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

class ImageDelegate(
    context: Context,
    private val listener: ClickListener
): BaseDelegate<ImageDelegate.ViewHolder, ImageWrapModel>(context) {

    private val selectedDrawable by lazy { ContextCompat.getDrawable(context, R.drawable.image_counter_selected) }
    private val deselectedDrawable by lazy { ContextCompat.getDrawable(context, R.drawable.image_counter_not_selected) }

    override fun onBindViewHolder(position: Int, item: ImageWrapModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.view.setOnClickListener { listener.onClick(position) }
        if(item.isCurrentlySelected) holder.tint.visibility = View.VISIBLE
        else holder.tint.visibility = View.GONE

        if(item.isMultipleSelectEnabled) {
            holder.counter.visibility = View.VISIBLE
            if(item.countNumber != 0) {
                holder.counter.text = item.countNumber.toString()
                holder.counter.background = selectedDrawable
            } else {
                holder.counter.text = ""
                holder.counter.background = deselectedDrawable
            }
        } else holder.counter.visibility = View.GONE

        holder.counter.setOnClickListener { listener.onMultipleSelectClick(position) }

        Glide.with(context)
            .load(File(item.id))
            .into(holder.image)
    }

    override fun isForViewType(item: Any?): Boolean = item is ImageWrapModel

    override fun onCreateViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup?): ViewHolder {
        return ViewHolder(
            layoutInflater.inflate(
                R.layout.item_image,
                parent,
                false
            )
        )
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.imageView_photo
        val tint: View = view.view_tint
        val counter: TextView = view.textView_counter
    }

    interface ClickListener {
        fun onClick(position: Int)
        fun onMultipleSelectClick(position: Int)
    }
}