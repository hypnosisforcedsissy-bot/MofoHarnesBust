package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ActivityEntity
import com.example.data.ChatMessageEntity
import com.example.data.DataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataRepository) : ViewModel() {
    val uiState: StateFlow<List<ActivityEntity>> = repository.allActivities
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.allChatMessages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun sendMessage(sender: String, message: String, embedding: String? = null, tags: String? = null) {
        viewModelScope.launch {
            repository.insertChatMessage(ChatMessageEntity(sender = sender, message = message, embedding = embedding, tags = tags))
        }
    }
}
