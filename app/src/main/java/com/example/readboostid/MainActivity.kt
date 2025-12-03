// File: MainActivity.kt
package com.readboost.id

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.readboost.id.presentation.navigation.NavGraph
import com.readboost.id.ui.theme.ReadBoostTheme

/**
 * ReadBoost ID - Aplikasi Literasi Digital
 * Tugas Besar PAB 2025
 *
 * Anggota Kelompok:
 * 1. Nama: [NAMA ANGGOTA 1], NIM: [NIM ANGGOTA 1]
 * 2. Nama: [NAMA ANGGOTA 2], NIM: [NIM ANGGOTA 2]
 * 3. Nama: [NAMA ANGGOTA 3], NIM: [NIM ANGGOTA 3]
 * 4. Nama: [NAMA ANGGOTA 4], NIM: [NIM ANGGOTA 4]
 *
 * Username & Password (jika diperlukan):
 * - Admin: username: admin, password: admin123
 * - User: username: user, password: user123
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContent {
                ReadBoostTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Temporary simple UI for debugging
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "ReadBoost ID - Debug Mode")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                // Navigate to actual app
                                setContent {
                                    ReadBoostTheme {
                                        Surface(
                                            modifier = Modifier.fillMaxSize(),
                                            color = MaterialTheme.colorScheme.background
                                        ) {
                                            val navController = rememberNavController()
                                            NavGraph(navController = navController)
                                        }
                                    }
                                }
                            }) {
                                Text("Start App")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MainActivity", "Critical error in setContent", e)
            // Show error to user if needed
            finish()
        }
    }
}