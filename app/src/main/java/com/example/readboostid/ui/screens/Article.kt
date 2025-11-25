package com.example.readboostid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.readboostid.ui.theme.ReadBoostIDTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen() {
    var isReading by remember { mutableStateOf(false) }
    var readingTime by remember { mutableStateOf(0) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(16) }
    var isDarkMode by remember { mutableStateOf(false) }

    // Timer effect
    LaunchedEffect(isReading) {
        while (isReading) {
            delay(1000)
            readingTime++
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Artikel") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Back logic */ }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isDarkMode = !isDarkMode }) {
                        Icon(
                            if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            "Toggle Mode"
                        )
                    }
                    IconButton(onClick = { showNoteDialog = true }) {
                        Icon(Icons.Default.Edit, "Add Note")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = if (isDarkMode) Color(0xFF1E1E1E) else MaterialTheme.colorScheme.surface,
                contentColor = if (isDarkMode) Color.White else MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Font size controls
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (fontSize > 12) fontSize-- }) {
                            Icon(Icons.Default.TextDecrease, "Smaller")
                        }
                        Text("${fontSize}sp", fontWeight = FontWeight.Medium)
                        IconButton(onClick = { if (fontSize < 24) fontSize++ }) {
                            Icon(Icons.Default.TextIncrease, "Larger")
                        }
                    }

                    // Reading control button
                    FilledTonalButton(
                        onClick = { isReading = !isReading },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isReading) Color(0xFFFF6B6B) else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            if (isReading) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isReading) "Jeda" else "Mulai")
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkMode) Color(0xFF121212) else MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Timer Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkMode)
                                Color(0xFF1E1E1E)
                            else
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Waktu Membaca",
                                    fontSize = 14.sp,
                                    color = if (isDarkMode) Color.White.copy(0.7f)
                                    else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    "${readingTime / 60}:${
                                        String.format(
                                            "%02d",
                                            readingTime % 60
                                        )
                                    }",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkMode) Color.White
                                    else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isReading) {
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFF4CAF50),
                                                    Color(0xFF81C784)
                                                )
                                            )
                                        } else {
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFF9E9E9E),
                                                    Color(0xFFBDBDBD)
                                                )
                                            )
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        if (isReading) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text(
                                        if (isReading) "Aktif" else "Jeda",
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Reward Info
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoChip(
                            icon = Icons.Default.AccessTime,
                            text = "5 menit",
                            color = Color(0xFF2196F3)
                        )
                        InfoChip(
                            icon = Icons.Default.Star,
                            text = "+50 XP",
                            color = Color(0xFFFFC107)
                        )
                        InfoChip(
                            icon = Icons.Default.Category,
                            text = "Motivasi",
                            color = Color(0xFF9C27B0)
                        )
                    }
                }

                // Article Title
                item {
                    Text(
                        "5 Cara Meningkatkan Konsentrasi Saat Membaca",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp,
                        color = if (isDarkMode) Color.White else MaterialTheme.colorScheme.onBackground
                    )
                }

                // Article Content
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkMode) Color(0xFF2C2C2C)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ArticleParagraph(
                                "Membaca adalah kunci untuk membuka pintu pengetahuan. Namun, banyak orang mengalami kesulitan untuk berkonsentrasi saat membaca, terutama di era digital yang penuh distraksi.",
                                fontSize,
                                isDarkMode
                            )
                            ArticleParagraph(
                                "1. Ciptakan Lingkungan yang Nyaman\n\nPastikan ruangan tempat Anda membaca memiliki pencahayaan yang cukup, tidak terlalu bising, dan suhu yang nyaman. Lingkungan yang kondusif akan membantu Anda fokus lebih lama.",
                                fontSize,
                                isDarkMode
                            )
                            ArticleParagraph(
                                "2. Gunakan Teknik Pomodoro\n\nBaca selama 25 menit, lalu istirahat 5 menit. Teknik ini membantu menjaga konsentrasi dan mencegah kelelahan mental.",
                                fontSize,
                                isDarkMode
                            )
                            ArticleParagraph(
                                "3. Matikan Notifikasi\n\nJauhkan smartphone atau matikan notifikasi saat membaca. Gangguan dari perangkat elektronik adalah musuh utama konsentrasi.",
                                fontSize,
                                isDarkMode
                            )
                            ArticleParagraph(
                                "4. Buat Catatan Singkat\n\nMenulis poin-poin penting saat membaca membantu otak tetap aktif dan meningkatkan pemahaman.",
                                fontSize,
                                isDarkMode
                            )
                            ArticleParagraph(
                                "5. Latih Secara Rutin\n\nSeperti otot, konsentrasi perlu dilatih. Mulai dengan durasi pendek dan tingkatkan secara bertahap.",
                                fontSize,
                                isDarkMode
                            )
                        }
                    }
                }

                // Action Buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* TODO: AI Summarize */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ringkas AI")
                        }

                        Button(
                            onClick = { showNoteDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.NoteAdd, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tulis Catatan")
                        }
                    }
                }
            }

            // Eye Rest Reminder
            if (readingTime > 0 && readingTime % 1200 == 0 && readingTime < 1203) { // Every 20 minutes (show for 3s)
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.RemoveRedEye,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "💡 Waktunya istirahatkan mata!",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Note Dialog
    if (showNoteDialog) {
        AddNoteDialog(
            onDismiss = { showNoteDialog = false },
            onSave = { note ->
                // TODO: Save note logic
                showNoteDialog = false
            }
        )
    }
}

@Composable
fun ArticleParagraph(text: String, fontSize: Int, isDark: Boolean) {
    Text(
        text = text,
        fontSize = fontSize.sp,
        lineHeight = (fontSize + 8).sp,
        color = if (isDark) Color.White.copy(0.9f) else MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun InfoChip(icon: ImageVector, text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = color
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text,
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun AddNoteDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var noteText by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Tambah Catatan", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Tulis catatanmu di sini...") },
                    modifier = Modifier.height(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onSave(noteText) }) {
                        Text("Simpan")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArticleDetailScreenPreview() {
    ReadBoostIDTheme {
        ArticleDetailScreen()
    }
}