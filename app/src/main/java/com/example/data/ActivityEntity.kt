package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_items")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subtitle: String,
    val timestamp: Long = System.currentTimeMillis()
)
