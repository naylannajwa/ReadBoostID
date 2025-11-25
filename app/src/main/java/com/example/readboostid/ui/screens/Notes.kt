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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.readboostid.ui.theme.ReadBoostIDTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Salin semua kode di bawah ini dan gantikan seluruh isi file Notes.kt Anda
// --- Data Model ---
data class Note(
    val id: Int,
    val articleTitle: String,
    val content: String,
    val dateCreated: Long,
    val category: String
)

// --- Main Screen Composable ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen() {
    // --- State Management ---
    var notes by remember { mutableStateOf(getDummyNotes()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredNotes = notes.filter {
        it.articleTitle.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true)
    }

    // --- UI Structure ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Catatan Saya",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${notes.size} catatan tersimpan",
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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingNote = null // Pastikan tidak ada note yang sedang diedit
                    showAddDialog = true
                },
                icon = { Icon(Icons.Default.Add, "Add Note") },
                text = { Text("Catatan Baru") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Cari catatan...") },
                leadingIcon = { Icon(Icons.Default.Search, "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Stats Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    icon = Icons.Default.Description,
                    value = "${notes.size}",
                    label = "Catatan",
                    color = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Default.Today,
                    value = "${notes.count { isToday(it.dateCreated) }}",
                    label = "Hari Ini",
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Default.Category,
                    value = "${notes
                        .map { it.category }
                        .distinct().size}",
                    label = "Kategori",
                    color = Color(0xFF9C27B0),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notes List
            if (filteredNotes.isEmpty()) {
                EmptyState(searchQuery = searchQuery)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredNotes, key = { it.id }) { note ->
                        NoteCard(
                            note = note,
                            onEdit = { editingNote = it },
                            onDelete = { noteToDelete = it }
                        )
                    }
                }
            }
        }
    }

    // --- Dialogs ---
    if (showAddDialog || editingNote != null) {
        NoteEditorDialog(
            note = editingNote,
            onDismiss = {
                showAddDialog = false
                editingNote = null
            },
            onSave = { title, content, category ->
                if (editingNote != null) {
                    // Update existing note
                    notes = notes.map {
                        if (it.id == editingNote!!.id) {
                            it.copy(
                                articleTitle = title,
                                content = content,
                                category = category
                            )
                        } else it
                    }
                } else {
                    // Add new note
                    val newNote = Note(
                        id = (notes.maxOfOrNull { it.id } ?: 0) + 1,
                        articleTitle = title,
                        content = content,
                        dateCreated = System.currentTimeMillis(),
                        category = category
                    )
                    notes = notes + newNote
                }
                showAddDialog = false
                editingNote = null
            }
        )
    }

    if (noteToDelete != null) {
        DeleteConfirmationDialog(
            onDismiss = { noteToDelete = null },
            onConfirm = {
                notes = notes.filter { it.id != noteToDelete!!.id }
                noteToDelete = null
            }
        )
    }
}

// --- Supporting Composables ---
@Composable
fun NoteCard(
    note: Note,
    onEdit: (Note) -> Unit,
    onDelete: (Note) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = getCategoryColor(note.category).copy(alpha = 0.15f)
                ) {
                    Text(
                        note.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = getCategoryColor(note.category),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    note.articleTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    note.content,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule, null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        formatDate(note.dateCreated),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { onEdit(note) }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.Edit,
                        "Edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = { onDelete(note) }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        "Delete",
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorDialog(
    note: Note?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember(note) { mutableStateOf(note?.articleTitle ?: "") }
    var content by remember(note) { mutableStateOf(note?.content ?: "") }
    var category by remember(note) { mutableStateOf(note?.category ?: "Umum") }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Motivasi", "Sejarah", "Teknologi", "Sains", "Umum")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (note == null) "➕ Catatan Baru" else "✏️ Edit Catatan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Artikel") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        label = { Text("Kategori") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Isi Catatan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(title, content, category) }) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
fun DeleteConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Warning, null, tint = Color(0xFFFF6B6B)) },
        title = { Text("Hapus Catatan?") },
        text = { Text("Catatan ini akan dihapus permanen. Tindakan ini tidak dapat dibatalkan.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B))
            ) { Text("Hapus") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
fun EmptyState(searchQuery: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.NoteAlt, null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.3f)
            )
            Text(
                if (searchQuery.isEmpty()) "Belum ada catatan" else "Tidak ada hasil",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                if (searchQuery.isEmpty()) "Mulai buat catatan dari artikel yang kamu baca" else "Coba kata kunci lain",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- Helper Functions ---
private fun getDummyNotes(): List<Note> {
    return listOf(
        Note(
            1,
            "5 Cara Meningkatkan Konsentrasi",
            "Poin penting: Lingkungan nyaman, teknik Pomodoro 25 menit, matikan notifikasi, buat catatan singkat, latihan rutin",
            System.currentTimeMillis(),
            "Motivasi"
        ),
        Note(
            2,
            "Sejarah Singkat Nusantara",
            "Kerajaan Majapahit adalah kerajaan terbesar di Nusantara. Dipimpin oleh Hayam Wuruk dengan Patih Gajah Mada.",
            System.currentTimeMillis() - 86400000,
            "Sejarah"
        ),
        Note(
            3,
            "Teknologi AI di Indonesia",
            "AI mulai berkembang di Indonesia. Banyak startup menggunakan machine learning untuk berbagai solusi bisnis.",
            System.currentTimeMillis() - 172800000,
            "Teknologi"
        )
    )
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun isToday(timestamp: Long): Boolean {
    val cal = Calendar.getInstance()
    val today = cal.clone() as Calendar
    cal.timeInMillis = timestamp
    return today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)
}

private fun getCategoryColor(category: String): Color {
    return when (category) {
        "Motivasi" -> Color(0xFF4CAF50)
        "Sejarah" -> Color(0xFFF44336)
        "Teknologi" -> Color(0xFF2196F3)
        "Sains" -> Color(0xFFFFC107)
        else -> Color(0xFF607D8B)
    }
}

// --- Preview Composable ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotesScreenPreview() {
    ReadBoostIDTheme {
        NotesScreen()
    }
}