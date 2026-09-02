package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
  private lateinit var appContainer: AppContainer

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    appContainer = AppContainer(applicationContext)
    val viewModel = MainViewModel(appContainer.repository)
    
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        ProfessionalPolishScreen(viewModel)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalPolishScreen(viewModel: MainViewModel) {
  val activities by viewModel.uiState.collectAsStateWithLifecycle()
  
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Open Code V2") },
        navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, null) } },
        actions = {
          IconButton(onClick = {}) { Icon(Icons.Default.Settings, null) }
        }
      )
    }
  ) { innerPadding ->
    Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Compact Dashboard
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
              Text("System Integration", color = MaterialTheme.colorScheme.primary)
              Box(modifier = Modifier.size(8.dp).background(StatusConnected, CircleShape))
            }
            Text("API Linked", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          }
        }

        // Permanent Chat Area
        ChatScreenArea(Modifier.weight(1f), viewModel)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenArea(modifier: Modifier = Modifier, viewModel: MainViewModel) {
  var text by remember { mutableStateOf("") }
  val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
  var showSelector by remember { mutableStateOf(false) }
  
  val models = listOf("Gemini 3.1 Flash Lite", "Gemini 1.5 Pro")
  var selectedModel by remember { mutableStateOf(models[0]) }
  var expanded by remember { mutableStateOf(false) }

  fun executeCommand(command: String): String {
      return when (command) {
          "/skill" -> "Executing custom skill: Performance Optimization."
          "/plugin" -> "Loading OpenCode plugin: Data Sync v2.1."
          else -> "Unknown command: $command"
      }
  }

  Column(modifier = modifier.fillMaxWidth()) {
    // Model Selector
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedModel,
            onValueChange = {},
            readOnly = true,
            label = { Text("Model") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            models.forEach { model ->
                DropdownMenuItem(
                    text = { Text(model) },
                    onClick = {
                        selectedModel = model
                        expanded = false
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    
    // Skill/Plugin Selector
    if (showSelector) {
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Select Skill/Plugin:", style = MaterialTheme.typography.labelLarge)
                listOf("/skill", "/plugin").forEach { cmd ->
                    TextButton(onClick = { 
                        text = cmd 
                        showSelector = false
                    }) {
                        Text(cmd)
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.weight(1f).fillMaxWidth().background(SurfaceBrightDark, RoundedCornerShape(16.dp)).padding(16.dp)) {
        LazyColumn {
            items(messages) { message ->
                Text("${message.sender}: ${message.message}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
      value = text,
      onValueChange = { text = it },
      modifier = Modifier.fillMaxWidth(),
      placeholder = { Text("Enter code or message (e.g., /skill)...") },
      leadingIcon = {
          IconButton(onClick = { showSelector = !showSelector }) {
              Icon(Icons.Default.Add, contentDescription = "Clap/Select")
          }
      },
      trailingIcon = { IconButton(onClick = {
          if (text.isNotBlank()) {
              if (text.startsWith("/")) {
                  viewModel.sendMessage("User", text)
                  val response = executeCommand(text)
                  viewModel.sendMessage("System", response)
              } else {
                  viewModel.sendMessage("User", text)
              }
              text = ""
          }
      }) { Icon(Icons.Default.Send, null) } }
    )
  }
}

@Composable
fun ActivityItem(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
  ListItem(
    modifier = Modifier.background(SurfaceBrightDark, RoundedCornerShape(16.dp)),
    leadingContent = {
      Surface(shape = RoundedCornerShape(12.dp), color = OnPrimaryDark) {
        Icon(icon, null, modifier = Modifier.padding(8.dp), tint = MaterialTheme.colorScheme.primary)
      }
    },
    headlineContent = { Text(title) },
    supportingContent = { Text(subtitle) },
    trailingContent = { Icon(Icons.Default.MoreVert, null) }
  )
}
