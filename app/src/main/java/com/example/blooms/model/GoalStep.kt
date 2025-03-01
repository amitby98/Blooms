package com.example.blooms.model

import java.io.Serializable

data class GoalStep(
    val text: String = "",
    var isChecked: Boolean = false): Serializable
