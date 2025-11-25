package com.example.readboostid.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.readboostid.ui.theme.ReadBoostIDTheme

data class Badge(
    val id: Int,
    val name: String,
    val description: String,
    val emoji: String,
    val requirement: String,
    val isUnlocked: Boolean,
    val unlockedDate: String? = null,
    val rarity: BadgeRarity
)

enum class BadgeRarity(val color: Color) {
    COMMON(Color(0xFF9E9E9E)),
    RARE(Color(0xFF2196F3)),
    EPIC(Color(0xFF9C27B0)),
    LEGENDARY(Color(0xFFFFD700))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgesScreen() {
    val badges = remember {
        listOf(
            Badge(1, "Pemula", "Membaca artikel pertama", "🥉", "Baca 1 artikel", true, "24 Nov 2025", BadgeRarity.COMMON),
            Badge(2, "Rajin Baca", "Membaca 10 artikel", "📚", "Baca 10 artikel", true, "25 Nov 2025", BadgeRarity.COMMON),
            Badge(3, "Week Warrior", "Streak 7 hari", "🔥", "Streak 7 hari berturut-turut", true, "26 Nov 2025", BadgeRarity.RARE),
            Badge(4, "Speed Reader", "Selesai artikel < 2 menit", "⚡", "Selesaikan artikel dalam 2 menit", false, null, BadgeRarity.RARE),
            Badge(5, "Night Owl", "Baca pukul 00:00-04:00", "🦉", "Membaca saat tengah malam", false, null, BadgeRarity.EPIC),
            Badge(6, "Marathon 30", "Streak 30 hari", "🏆", "Streak 30 hari berturut-turut", false, null, BadgeRarity.EPIC),
            Badge(7, "Pembaca Master", "Baca 50 artikel", "👑", "Selesaikan 50 artikel", false, null, BadgeRarity.LEGENDARY),
            Badge(8, "Explorer", "Baca 5 kategori berbeda", "🌍", "Jelajahi 5 kategori", false, null, BadgeRarity.RARE),
            Badge(9, "Note Taker", "Buat 20 catatan", "✍️", "Tulis 20 catatan ringkasan", true, "25 Nov 2025", BadgeRarity.COMMON),
            Badge(10, "Level 10", "Capai Level 10", "⭐", "Dapatkan 5000 XP", false, null, BadgeRarity.EPIC),
            Badge(11, "Early Bird", "Baca pukul 05:00-07:00", "🌅", "Membaca di pagi hari", true, "26 Nov 2025", BadgeRarity.RARE),
            Badge(12, "Nusantara Reader", "Baca 100 artikel", "🇮🇩", "Selesaikan 100 artikel", false, null, BadgeRarity.LEGENDARY)
        )
    }

    val unlockedCount = badges.count { it.isUnlocked }
    val totalBadges = badges.size
    val progress = unlockedCount.toFloat() / totalBadges

    var selectedBadge by remember { mutableStateOf<Badge?>(null) }
    var selectedFilter by remember { mutableStateOf("Semua") }

    val filteredBadges = when (selectedFilter) {
        "Terbuka" -> badges.filter { it.isUnlocked }
        "Terkunci" -> badges.filter { !it.isUnlocked }
        else -> badges
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Achievements",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "$unlockedCount dari $totalBadges badge",
                            fontSize = 12.sp,
                            color = Color.White.copy(0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Progress Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Progress Badge",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "${(progress * 100).toInt()}% Selesai",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "$unlockedCount",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "Semua",
                    onClick = { selectedFilter = "Semua" },
                    label = { Text("Semua ($totalBadges)") }
                )
                FilterChip(
                    selected = selectedFilter == "Terbuka",
                    onClick = { selectedFilter = "Terbuka" },
                    label = { Text("Terbuka ($unlockedCount)") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
                FilterChip(
                    selected = selectedFilter == "Terkunci",
                    onClick = { selectedFilter = "Terkunci" },
                    label = { Text("Terkunci (${totalBadges - unlockedCount})") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Badges Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredBadges) { badge ->
                    BadgeCard(
                        badge = badge,
                        onClick = { selectedBadge = badge }
                    )
                }
            }
        }
    }

    // Badge Detail Dialog
    selectedBadge?.let { badge ->
        BadgeDetailDialog(
            badge = badge,
            onDismiss = { selectedBadge = null }
        )
    }
}

@Composable
fun BadgeCard(
    badge: Badge,
    onClick: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    val infiniteTransition = rememberInfiniteTransition(label = "badge")

    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked)
                badge.rarity.color.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (badge.isUnlocked) 4.dp else 1.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Badge Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            if (badge.isUnlocked) {
                                Brush.radialGradient(
                                    colors = listOf(
                                        badge.rarity.color.copy(alpha = 0.3f),
                                        badge.rarity.color.copy(alpha = 0.1f)
                                    )
                                )
                            } else {
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color.Gray.copy(alpha = 0.2f),
                                        Color.Gray.copy(alpha = 0.1f)
                                    )
                                )
                            }
                        )
                        .alpha(if (badge.isUnlocked) shimmer else 0.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        badge.emoji,
                        fontSize = 40.sp,
                        modifier = Modifier.alpha(if (badge.isUnlocked) 1f else 0.3f)
                    )

                    if (!badge.isUnlocked) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Locked",
                            modifier = Modifier
                                .size(32.dp)
                                .offset(y = 20.dp),
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Badge Name
                Text(
                    badge.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    color = if (badge.isUnlocked)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rarity Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badge.rarity.color.copy(alpha = if (badge.isUnlocked) 0.2f else 0.1f)
                ) {
                    Text(
                        when (badge.rarity) {
                            BadgeRarity.COMMON -> "Common"
                            BadgeRarity.RARE -> "Rare"
                            BadgeRarity.EPIC -> "Epic"
                            BadgeRarity.LEGENDARY -> "Legendary"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (badge.isUnlocked) badge.rarity.color else Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // Checkmark for unlocked badges
            if (badge.isUnlocked) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Unlocked",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp),
                    tint = badge.rarity.color
                )
            }
        }
    }
}

@Composable
fun BadgeDetailDialog(
    badge: Badge,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                badge.rarity.color.copy(alpha = 0.3f),
                                badge.rarity.color.copy(alpha = 0.1f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    badge.emoji,
                    fontSize = 56.sp
                )
                if (!badge.isUnlocked) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Locked",
                        modifier = Modifier
                            .size(40.dp)
                            .offset(y = 25.dp),
                        tint = Color.Gray
                    )
                }
            }
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    badge.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badge.rarity.color.copy(alpha = 0.2f)
                ) {
                    Text(
                        when (badge.rarity) {
                            BadgeRarity.COMMON -> "⚪ Common"
                            BadgeRarity.RARE -> "🔵 Rare"
                            BadgeRarity.EPIC -> "🟣 Epic"
                            BadgeRarity.LEGENDARY -> "🟡 Legendary"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = badge.rarity.color,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    badge.description,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Divider()

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Flag,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        badge.requirement,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (badge.isUnlocked && badge.unlockedDate != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Dibuka pada ${badge.unlockedDate}",
                            fontSize = 14.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BadgesScreenPreview() {
    ReadBoostIDTheme {
        BadgesScreen()
    }
}