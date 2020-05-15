package com.akvelon.imagepicker

data class ImageWrapModel(
    val id: String,
    var isCurrentlySelected: Boolean,
    var isMultipleSelectEnabled: Boolean,
    var countNumber: Int = 0
)