package com.example.blooms.model.dao

import androidx.room.TypeConverter
import com.example.blooms.model.GoalStep
import com.example.blooms.model.Post
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromPostList(value: ArrayList<Post>?): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toPostList(value: String): ArrayList<Post> {
        val gson = Gson()
        val listType = object : TypeToken<ArrayList<Post>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromGoalStepList(value: ArrayList<GoalStep>?): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toGoalStepList(value: String): ArrayList<GoalStep> {
        val gson = Gson()
        val listType = object : TypeToken<ArrayList<GoalStep>>() {}.type
        return gson.fromJson(value, listType)
    }
}
