package com.controlself.edu.ui.screens.login

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.R
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
import com.controlself.edu.ui.theme.CsePrimaryFixedDim
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseWhite
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoggedIn: (Session) -> Unit,
    onCreateAccount: () -> Unit,
    onForgotPassword: () -> Unit
) {
    val auth = LocalAppContainer.current.authRepository
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberSession by remember { mutableStateOf(true) }
    var role by remember { mutableStateOf(UserRole.STUDENT) }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LoginContent(
        username = username,
        onUsernameChange = { username = it; error = null },
        password = password,
        onPasswordChange = { password = it; error = null },
        rememberSession = rememberSession,
        onRememberSessionChange = { rememberSession = it },
        role = role,
        onRoleChange = { role = it },
        passwordVisible = passwordVisible,
        onPasswordVisibleChange = { passwordVisible = it },
        loading = loading,
        error = error,
        onCreateAccount = onCreateAccount,
        onForgotPassword = onForgotPassword,
        onSubmit = {
            loading = true
            error = null
            scope.launch {
                val result = auth.login(username, password, role, rememberSession)
                loading = false
                result.fold(
                    onSuccess = onLoggedIn,
                    onFailure = { error = it.message ?: "Error al iniciar sesión" }
                )
            }
        }
    )
}

@Composable
private fun LoginContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    rememberSession: Boolean,
    onRememberSessionChange: (Boolean) -> Unit,
    role: UserRole,
    onRoleChange: (UserRole) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    loading: Boolean,
    error: String?,
    onCreateAccount: () -> Unit,
    onForgotPassword: () -> Unit,
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
            .padding(top = 48.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "Logo ControlSelf EDU",
            modifier = Modifier.size(96.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ControlSelf EDU",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Gestión educativa segura para todos.",
            style = MaterialTheme.typography.bodyMedium,
            color = CseOnSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))

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
            Text(
                text = "Usuario o correo",
                style = MaterialTheme.typography.labelLarge,
                color = CseOnSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.labelLarge,
                    color = CseOnSurface,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onForgotPassword, enabled = !loading) {
                    Text("¿Olvidaste tu contraseña?", color = CsePrimary)
                }
            }
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
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recordar sesión", style = MaterialTheme.typography.bodyMedium)
                Switch(
                    checked = rememberSession,
                    onCheckedChange = onRememberSessionChange,
                    colors = SwitchDefaults.colors(checkedTrackColor = CseSecondary)
                )
            }
            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryFlatButton(
                text = if (loading) "…" else "Entrar",
                onClick = onSubmit,
                enabled = !loading,
                containerColor = actionColor
            )
            if (loading) {
                Spacer(modifier = Modifier.height(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.CenterHorizontally),
                    strokeWidth = 2.dp,
                    color = actionColor
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "¿No tienes una cuenta?",
                style = MaterialTheme.typography.labelMedium,
                color = CseOnSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            TextButton(
                onClick = onCreateAccount,
                enabled = !loading,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Regístrate ahora", color = CsePrimary, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Demo: estudiante / docente / padre — clave 123456",
            style = MaterialTheme.typography.labelMedium,
            color = CseOnSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    ControlSelfEDUTheme {
        LoginContent(
            username = "estudiante",
            onUsernameChange = {},
            password = "",
            onPasswordChange = {},
            rememberSession = true,
            onRememberSessionChange = {},
            role = UserRole.STUDENT,
            onRoleChange = {},
            passwordVisible = false,
            onPasswordVisibleChange = {},
            loading = false,
            error = null,
            onCreateAccount = {},
            onForgotPassword = {},
            onSubmit = {}
        )
    }
}
