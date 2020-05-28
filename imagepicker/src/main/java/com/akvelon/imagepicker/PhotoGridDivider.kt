package com.akvelon.imagepicker

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PhotoGridDivider(private val size: Int, private val columnCount: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = size
        outRect.right = size
        val position = parent.getChildAdapterPosition(view)
        if(position % columnCount == 0) {
            outRect.left = size
        }
    }
}