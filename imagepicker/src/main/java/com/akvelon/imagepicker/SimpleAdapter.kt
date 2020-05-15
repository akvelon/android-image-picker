package com.akvelon.imagepicker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

open class SimpleAdapter(
    val items: List<*>,
    val delegatesManager: AdapterDelegatesManager<List<*>>
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder = delegatesManager.onCreateViewHolder(parent, viewType)

    override fun getItemViewType(position: Int): Int
            = delegatesManager.getItemViewType(items, position)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(items, position, holder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int,
                                  payloads: MutableList<Any>) {
        delegatesManager.onBindViewHolder(items, position, holder, payloads)
    }
}