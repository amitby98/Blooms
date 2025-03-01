package com.example.blooms.general

import com.example.blooms.R
import com.example.blooms.model.Category

object Constance {
    /* Category ID - add new goal */
    const val FITNESS = 1
    const val ECONOMY = 2
    const val FAMILY = 3
    const val SHOPPING = 4
    const val VACATION = 5
    const val HEALTH = 6
    const val OTHER = 7


    val categories = mutableListOf(
        Category(FITNESS, "Fitness", R.drawable.fitness_running_icon),
        Category(ECONOMY, "Economy", R.drawable.economic),
        Category(FAMILY, "Family", R.drawable.family_care_father_mother_icon),
        Category(SHOPPING, "Shopping", R.drawable.shopping_bag),
        Category(VACATION, "Vacation", R.drawable.vacation),
        Category(HEALTH, "Health", R.drawable.health_healthcare_medical_icon),
        Category(OTHER, "Other", R.drawable.other_icon)
    )

    /*Const Key*/
    const val ADD_NEW_POST_FROM_GOAL = "ADD_NEW_POST_FROM_GOAL"

}
