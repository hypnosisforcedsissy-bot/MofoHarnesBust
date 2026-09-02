package com.example

import android.content.Context
import androidx.room.Room
import com.example.BuildConfig
import com.example.data.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AppContainer(context: Context) {
    val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).fallbackToDestructiveMigration().build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.OPENCODE_API_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    val repository: DataRepository = DataRepository(database.activityDao(), database.chatMessageDao(), apiService)
}
