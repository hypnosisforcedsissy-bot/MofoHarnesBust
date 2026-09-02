package com.example.data

import kotlinx.coroutines.flow.Flow

class DataRepository(
    private val activityDao: ActivityDao,
    private val chatMessageDao: ChatMessageDao,
    private val apiService: ApiService
) {
    val allActivities: Flow<List<ActivityEntity>> = activityDao.getAllActivities()
    val allChatMessages: Flow<List<ChatMessageEntity>> = chatMessageDao.getAllMessages()

    suspend fun insertChatMessage(message: ChatMessageEntity) {
        chatMessageDao.insertMessage(message)
    }

    suspend fun refreshActivities() {
        try {
            val activities = apiService.getRecentActivities()
            activityDao.insertAll(activities)
        } catch (e: Exception) {
            // Handle error
        }
    }
}
