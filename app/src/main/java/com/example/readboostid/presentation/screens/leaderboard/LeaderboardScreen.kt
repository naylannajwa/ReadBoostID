// File: presentation/screens/leaderboard/LeaderboardScreen.kt (UPDATED)
package com.readboost.id.presentation.screens.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readboost.id.ReadBoostApplication
import com.readboost.id.data.model.Leaderboard
import com.readboost.id.presentation.viewmodel.ViewModelFactory
import com.readboost.id.ui.theme.ReadBoostTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as ReadBoostApplication
    val viewModel: LeaderboardViewModel = viewModel(
        factory = ViewModelFactory(app.appContainer)
    )

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard Juara") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🏆",
                                style = MaterialTheme.typography.displayMedium
                            )
                            Text(
                                text = "Top Readers",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                itemsIndexed(uiState.leaderboard) { index, entry ->
                    LeaderboardItem(
                        rank = index + 1,
                        username = entry.username,
                        xp = entry.totalXP
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(rank: Int, username: String, xp: Int) {
    val rankIcon = when (rank) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> "$rank"
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = rankIcon,
                    style = if (rank <= 3) MaterialTheme.typography.headlineMedium
                    else MaterialTheme.typography.titleLarge,
                    modifier = Modifier.width(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = username,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (rank <= 3) FontWeight.Bold else FontWeight.Normal
                )
            }
            Text(
                text = "$xp XP",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    device = Devices.PIXEL_4,
    showSystemUi = true,
    name = "Leaderboard Screen - Full"
)
@Composable
fun LeaderboardScreenPreview() {
    ReadBoostTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Leaderboard Juara") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🏆",
                                style = MaterialTheme.typography.displayMedium
                            )
                            Text(
                                text = "Top Readers",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                itemsIndexed(listOf(
                    Leaderboard(userId = 1, username = "Budi Santoso", totalXP = 850, rank = 1),
                    Leaderboard(userId = 2, username = "Siti Nurhaliza", totalXP = 720, rank = 2),
                    Leaderboard(userId = 3, username = "Ahmad Fauzi", totalXP = 680, rank = 3),
                    Leaderboard(userId = 4, username = "Rina Wijaya", totalXP = 550, rank = 4),
                    Leaderboard(userId = 5, username = "Dedi Kurniawan", totalXP = 490, rank = 5)
                )) { index, entry ->
                    LeaderboardItem(
                        rank = index + 1,
                        username = entry.username,
                        xp = entry.totalXP
                    )
                }
            }
        }
    }
}