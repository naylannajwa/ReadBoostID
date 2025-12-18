// File: presentation/screens/admin/AdminRegisterViewModel.kt
package com.readboost.id.presentation.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readboost.id.data.local.UserPreferences
import com.readboost.id.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminRegisterUiState(
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false
)

class AdminRegisterViewModel(
    private val articleRepository: ArticleRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminRegisterUiState())
    val uiState: StateFlow<AdminRegisterUiState> = _uiState.asStateFlow()

    companion object {
        // In-memory storage for demo (in real app, use secure AdminUser table)
        private val adminCredentials = mutableMapOf<String, AdminData>()

        init {
            // Initialize with default admin
            adminCredentials["admin"] = AdminData("Administrator", "admin123")
            adminCredentials["superadmin"] = AdminData("Super Admin", "super123")
        }

        fun isValidAdmin(username: String, password: String): Boolean {
            val adminData = adminCredentials[username] as? AdminData
            return adminData?.password == password
        }

        fun getAdminName(username: String): String {
            val adminData = adminCredentials[username] as? AdminData
            return adminData?.name ?: "Admin"
        }

        fun registerNewAdmin(name: String, username: String, password: String): Boolean {
            if (adminCredentials.containsKey(username)) {
                return false // Username already exists
            }
            adminCredentials[username] = AdminData(name, password)
            return true
        }

        data class AdminData(val name: String, val password: String)
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = null,
            errorMessage = null
        )
    }

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
            usernameError = null,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            errorMessage = null
        )
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null,
            errorMessage = null
        )
    }

    fun register() {
        val currentState = _uiState.value

        // Validation
        var hasError = false
        var nameError: String? = null
        var usernameError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        if (currentState.name.isBlank()) {
            nameError = "Nama admin tidak boleh kosong"
            hasError = true
        }

        if (currentState.username.isBlank()) {
            usernameError = "Username admin tidak boleh kosong"
            hasError = true
        } else if (currentState.username.length < 3) {
            usernameError = "Username minimal 3 karakter"
            hasError = true
        } else if (adminCredentials.containsKey(currentState.username)) {
            usernameError = "Username admin sudah terdaftar"
            hasError = true
        }

        if (currentState.password.isBlank()) {
            passwordError = "Password tidak boleh kosong"
            hasError = true
        } else if (currentState.password.length < 6) {
            passwordError = "Password minimal 6 karakter"
            hasError = true
        }

        if (currentState.confirmPassword.isBlank()) {
            confirmPasswordError = "Konfirmasi password tidak boleh kosong"
            hasError = true
        } else if (currentState.password != currentState.confirmPassword) {
            confirmPasswordError = "Password tidak cocok"
            hasError = true
        }

        if (hasError) {
            _uiState.value = currentState.copy(
                nameError = nameError,
                usernameError = usernameError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
            return
        }

        // Start registration process
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                // Register new admin
                val success = registerNewAdmin(currentState.name, currentState.username, currentState.password)

                if (success) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        registrationSuccess = true,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal mendaftarkan admin"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Terjadi kesalahan: ${e.message}"
                )
            }
        }
    }

}
