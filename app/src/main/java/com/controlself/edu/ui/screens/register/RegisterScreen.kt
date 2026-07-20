package com.controlself.edu.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.data.auth.LocalAuthRepository
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Session
import com.controlself.edu.domain.model.UserRole
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.roleActionColor
import com.controlself.edu.ui.screens.auth.RoleSelector
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseOnSurface
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseWhite
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegistered: (Session) -> Unit,
    onBack: () -> Unit
) {
    val auth = LocalAppContainer.current.authRepository
    val scope = rememberCoroutineScope()

    var displayName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(UserRole.STUDENT) }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    RegisterContent(
        displayName = displayName,
        onDisplayNameChange = { displayName = it; error = null },
        username = username,
        onUsernameChange = { username = it; error = null },
        password = password,
        onPasswordChange = { password = it; error = null },
        role = role,
        onRoleChange = { role = it },
        passwordVisible = passwordVisible,
        onPasswordVisibleChange = { passwordVisible = it },
        loading = loading,
        error = error,
        onBack = onBack,
        onSubmit = {
            loading = true
            error = null
            scope.launch {
                val result = auth.register(displayName, username, password, role)
                loading = false
                result.fold(
                    onSuccess = onRegistered,
                    onFailure = { error = it.message ?: "No se pudo registrar" }
                )
            }
        }
    )
}

@Composable
private fun RegisterContent(
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    role: UserRole,
    onRoleChange: (UserRole) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    loading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    val actionColor = roleActionColor(
        isStudent = role == UserRole.STUDENT,
        isParent = role == UserRole.PARENT
    )
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = CsePrimary,
        unfocusedBorderColor = CseOutlineVariant,
        focusedContainerColor = CseBackground,
        unfocusedContainerColor = CseBackground
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 32.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = CsePrimary
            )
        }
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = "Elige tu rol y completa tus datos.",
            style = MaterialTheme.typography.bodyMedium,
            color = CseOnSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CseWhite)
                .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            RoleSelector(selected = role, onSelected = onRoleChange)
            Spacer(modifier = Modifier.height(20.dp))

            FieldLabel("Nombre")
            OutlinedTextField(
                value = displayName,
                onValueChange = onDisplayNameChange,
                placeholder = { Text("Tu nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )
            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("Usuario o correo")
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                placeholder = { Text("nombre@ejemplo.com") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("Contraseña (mín. ${LocalAuthRepository.MIN_PASSWORD_LENGTH})")
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                placeholder = { Text("••••••••") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            },
                            contentDescription = null,
                            tint = CseOnSurfaceVariant
                        )
                    }
                }
            )

            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            PrimaryFlatButton(
                text = if (loading) "…" else "Crear cuenta",
                onClick = onSubmit,
                enabled = !loading,
                containerColor = actionColor
            )
            if (loading) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 2.dp,
                        color = actionColor
                    )
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = CseOnSurface,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    ControlSelfEDUTheme {
        RegisterContent(
            displayName = "Ana",
            onDisplayNameChange = {},
            username = "",
            onUsernameChange = {},
            password = "",
            onPasswordChange = {},
            role = UserRole.STUDENT,
            onRoleChange = {},
            passwordVisible = false,
            onPasswordVisibleChange = {},
            loading = false,
            error = null,
            onBack = {},
            onSubmit = {}
        )
    }
}
