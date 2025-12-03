// File: presentation/screens/home/HomeScreen.kt
package com.readboost.id.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readboost.id.ReadBoostApplication
import com.readboost.id.data.model.Article
import com.readboost.id.data.model.UserProgress
import com.readboost.id.presentation.viewmodel.ViewModelFactory
import com.readboost.id.ui.theme.ReadBoostTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToArticleList: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToArticle: (Int) -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as? ReadBoostApplication

    // ViewModel initialization with proper factory
    val homeViewModel = viewModel<HomeViewModel>(
        factory = if (app?.isAppContainerInitialized == true) {
            ViewModelFactory(app.appContainer)
        } else {
            // Fallback - will use default factory
            null
        }
    )
    val uiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ReadBoost ID") },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToArticleList,
                    icon = { Icon(Icons.Default.List, contentDescription = "Articles") },
                    label = { Text("Articles") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToLeaderboard,
                    icon = { Icon(Icons.Default.Star, contentDescription = "Leaderboard") },
                    label = { Text("Leaderboard") }
                )
            }
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // User Progress Card
                item {
                    UserProgressCard(
                        totalXP = uiState.userProgress?.totalXP ?: 0,
                        streakDays = uiState.userProgress?.streakDays ?: 0,
                        dailyTarget = uiState.userProgress?.dailyTarget ?: 5
                    )
                }

                // Daily Target Section
                item {
                    Text(
                        text = "Target Harian",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    DailyTargetCard(
                        targetMinutes = uiState.userProgress?.dailyTarget ?: 5
                    )
                }

                // Recent Articles Section
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Artikel Terbaru",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = onNavigateToArticleList) {
                            Text("Lihat Semua")
                        }
                    }
                }

                items(uiState.articles) { article ->
                    ArticleCard(
                        article = article,
                        onClick = { onNavigateToArticle(article.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun UserProgressCard(
    totalXP: Int,
    streakDays: Int,
    dailyTarget: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Progress Kamu",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProgressItem(
                    icon = "⭐",
                    label = "Total XP",
                    value = totalXP.toString()
                )
                ProgressItem(
                    icon = "🔥",
                    label = "Streak",
                    value = "$streakDays hari"
                )
                ProgressItem(
                    icon = "🎯",
                    label = "Target",
                    value = "$dailyTarget min"
                )
            }
        }
    }
}

@Composable
fun ProgressItem(icon: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun DailyTargetCard(targetMinutes: Int) {
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
            Column {
                Text(
                    text = "Baca $targetMinutes menit hari ini",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Raih XP dan tingkatkan streak!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun ArticleCard(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.category,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = "${article.duration} min",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.content.take(100) + "...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "+${article.xp} XP",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Preview Functions - Full Screen
@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    device = Devices.PIXEL_4,
    showSystemUi = true,
    name = "Home Screen - Full"
)
@Composable
fun HomeScreenPreview() {
    ReadBoostTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("ReadBoost ID") },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Icon(Icons.Default.List, contentDescription = "Articles") },
                        label = { Text("Articles") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Icon(Icons.Default.Star, contentDescription = "Leaderboard") },
                        label = { Text("Leaderboard") }
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    UserProgressCard(
                        totalXP = 250,
                        streakDays = 7,
                        dailyTarget = 5
                    )
                }
                item {
                    Text(
                        text = "Target Harian",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    DailyTargetCard(targetMinutes = 5)
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Artikel Terbaru",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = {}) {
                            Text("Lihat Semua")
                        }
                    }
                }
                items(listOf(
                    Article(id = 1, title = "Memahami Kecerdasan Buatan (AI)", content = "Kecerdasan Buatan atau Artificial Intelligence (AI) adalah simulasi kecerdasan manusia yang diprogram dalam mesin. AI memungkinkan komputer untuk belajar dari pengalaman.", duration = 5, category = "Teknologi", difficulty = "Dasar", xp = 15),
                    Article(id = 2, title = "Psikologi Kebahagiaan", content = "Kebahagiaan adalah kondisi emosional yang ditandai dengan perasaan positif seperti kepuasan, kegembiraan, dan makna hidup.", duration = 4, category = "Psikologi", difficulty = "Dasar", xp = 12)
                )) { article ->
                    ArticleCard(article = article, onClick = {})
                }
            }
        }
    }
}

// Component Previews
@Preview(showBackground = true)
@Composable
fun UserProgressCardPreview() {
    ReadBoostTheme {
        UserProgressCard(
            totalXP = 250,
            streakDays = 7,
            dailyTarget = 5
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressItemPreview() {
    ReadBoostTheme {
        ProgressItem(
            icon = "⭐",
            label = "Total XP",
            value = "250"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DailyTargetCardPreview() {
    ReadBoostTheme {
        DailyTargetCard(targetMinutes = 5)
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleCardPreview() {
    ReadBoostTheme {
        ArticleCard(
            article = Article(
                id = 1,
                title = "Memahami Kecerdasan Buatan (AI)",
                content = "Kecerdasan Buatan atau Artificial Intelligence (AI) adalah simulasi kecerdasan manusia yang diprogram dalam mesin. AI memungkinkan komputer untuk belajar dari pengalaman, menyesuaikan dengan input baru, dan melakukan tugas-tugas yang biasanya memerlukan kecerdasan manusia.",
                duration = 5,
                category = "Teknologi",
                difficulty = "Dasar",
                xp = 15
            ),
            onClick = {}
        )
    }
}