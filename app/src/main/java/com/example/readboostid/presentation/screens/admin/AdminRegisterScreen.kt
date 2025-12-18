// File: presentation/screens/admin/AdminRegisterScreen.kt
package com.readboost.id.presentation.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readboost.id.ReadBoostApplication
import com.readboost.id.presentation.viewmodel.ViewModelFactory
import com.readboost.id.ui.theme.ReadBoostTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAdminLogin: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as? ReadBoostApplication

    val adminRegisterViewModel = viewModel<AdminRegisterViewModel>(
        factory = if (app?.isAppContainerInitialized == true) {
            ViewModelFactory(app.appContainer)
        } else {
            null
        }
    )

    val uiState by adminRegisterViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // Handle successful registration
    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            scope.launch {
                delay(1000) // Show success message briefly
                onNavigateToAdminLogin()
            }
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Header section with gradient background
                AdminRegisterHeaderSection(onNavigateBack)

                // Registration form
                AdminRegisterForm(
                    uiState = uiState,
                    onNameChange = adminRegisterViewModel::onNameChange,
                    onUsernameChange = adminRegisterViewModel::onUsernameChange,
                    onPasswordChange = adminRegisterViewModel::onPasswordChange,
                    onConfirmPasswordChange = adminRegisterViewModel::onConfirmPasswordChange,
                    onRegisterClick = { adminRegisterViewModel.register() },
                    onLoginClick = onNavigateToAdminLogin
                )
            }

            // Loading overlay
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFFFC107) // Gold color
                    )
                }
            }

            // Success message
            if (uiState.registrationSuccess) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Admin Berhasil Didaftarkan!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Kembali ke halaman login admin...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminRegisterHeaderSection(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1976D2), // Blue
                        Color(0xFF0D47A1)  // Darker blue
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back button and title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Admin Registration",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Shield icon
            Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = "Admin Registration",
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Welcome text
            Text(
                text = "Register New Admin",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Daftarkan akun admin baru untuk mengelola artikel",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRegisterForm(
    uiState: AdminRegisterUiState,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Name field
        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nama Lengkap Admin") },
            placeholder = { Text("Masukkan nama lengkap admin") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Name",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF1976D2) // Blue color
            ),
            singleLine = true,
            isError = uiState.nameError != null
        )

        if (uiState.nameError != null) {
            Text(
                text = uiState.nameError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username field
        OutlinedTextField(
            value = uiState.username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Admin Username") },
            placeholder = { Text("Masukkan username admin") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Username",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFFFFC107) // Gold color
            ),
            singleLine = true,
            isError = uiState.usernameError != null
        )

        if (uiState.usernameError != null) {
            Text(
                text = uiState.usernameError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password field
        var passwordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Admin Password") },
            placeholder = { Text("Minimal 6 karakter") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFFFFC107) // Gold color
            ),
            singleLine = true,
            isError = uiState.passwordError != null
        )

        if (uiState.passwordError != null) {
            Text(
                text = uiState.passwordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password field
        var confirmPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Konfirmasi Password") },
            placeholder = { Text("Ulangi password admin") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirm Password",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFFFFC107) // Gold color
            ),
            singleLine = true,
            isError = uiState.confirmPasswordError != null
        )

        if (uiState.confirmPasswordError != null) {
            Text(
                text = uiState.confirmPasswordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Register button
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2) // Blue color
            ),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Daftar Admin",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sudah punya akun admin? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "Login di sini",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2), // Blue color
                modifier = Modifier.clickable(onClick = onLoginClick)
            )
        }

        // General error message
        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AdminRegisterScreenPreview() {
    ReadBoostTheme {
        AdminRegisterScreen(
            onNavigateBack = {},
            onNavigateToAdminLogin = {}
        )
    }
}
