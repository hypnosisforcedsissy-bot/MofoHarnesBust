package com.example.data

import retrofit2.http.GET

interface ApiService {
    @GET("activities")
    suspend fun getRecentActivities(): List<ActivityEntity>
}
