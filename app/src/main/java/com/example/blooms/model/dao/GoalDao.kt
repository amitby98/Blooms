package com.example.blooms.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.blooms.model.Goal
import com.example.blooms.model.Post

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoals(goals: List<Goal>)

    @Query("SELECT * FROM goals")
    suspend fun getAllGoals(): List<Goal>

    @Query("SELECT * FROM goals WHERE userId = :userId")
    suspend fun getGoalByUserId(userId: String): List<Goal>

    @Delete
    suspend fun deleteGoal(goal: Goal)
}