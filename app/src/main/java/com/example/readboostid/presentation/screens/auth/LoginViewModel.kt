// File: presentation/screens/auth/LoginViewModel.kt
package com.readboost.id.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readboost.id.data.local.UserPreferences
import com.readboost.id.data.model.CurrentUser
import com.readboost.id.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

class LoginViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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

    fun login() {
        val currentState = _uiState.value

        // Validation
        var hasError = false
        var usernameError: String? = null
        var passwordError: String? = null

        if (currentState.username.isBlank()) {
            usernameError = "Username tidak boleh kosong"
            hasError = true
        }

        if (currentState.password.isBlank()) {
            passwordError = "Password tidak boleh kosong"
            hasError = true
        }

        if (hasError) {
            _uiState.value = currentState.copy(
                usernameError = usernameError,
                passwordError = passwordError
            )
            return
        }

        // Start login process
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                val passwordHash = hashPassword(currentState.password)
                val result = userRepository.loginUser(currentState.username, passwordHash)

                result.onSuccess { user ->
                    if (user != null) {
                        // Save user data to preferences
                        val currentUser = CurrentUser(
                            id = user.id,
                            username = user.username,
                            fullName = user.fullName,
                            email = user.email
                        )
                        userPreferences.saveCurrentUser(currentUser)

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            loginSuccess = true,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Username atau password salah"
                        )
                    }
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Terjadi kesalahan: ${exception.message}"
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

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
