package com.example.blooms.model

import java.io.Serializable

data class GoalStep(
    var text: String = "",
    var isChecked: Boolean = false): Serializable
