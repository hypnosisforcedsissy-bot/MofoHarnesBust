package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val embedding: String? = null,
    val tags: String? = null
)
