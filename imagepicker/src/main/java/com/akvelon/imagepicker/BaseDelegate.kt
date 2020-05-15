package com.akvelon.imagepicker

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate

abstract class BaseDelegate<VH: RecyclerView.ViewHolder, in ItemType>(val context: Context)
    : AdapterDelegate<List<*>>() {

    final override fun onBindViewHolder(
        items: List<*>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>) {
        val viewHolder = holder as? VH ?: return
        val item = items[position] as? ItemType ?: return
        onBindViewHolder(position, item, viewHolder, payloads)
    }

    abstract fun onBindViewHolder(position: Int, item: ItemType, holder: VH, payloads: MutableList<Any>)

    final override fun isForViewType(items: List<*>, position: Int): Boolean = isForViewType(items[position])

    abstract fun isForViewType(item: Any?): Boolean

    final override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        return onCreateViewHolder(LayoutInflater.from(context), parent)
    }

    abstract fun onCreateViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup?): VH
}