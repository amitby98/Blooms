package com.example.blooms.model

data class Category(
    val id: Int = -1,
    val name: String = "",
    val icon: Int = -1,
    var isSelected: Boolean = false
)
