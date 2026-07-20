package com.controlself.edu.ui.screens.forgot

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseOnSurface
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseWhite
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(onBack: () -> Unit) {
    val auth = LocalAppContainer.current.authRepository
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    ForgotPasswordContent(
        username = username,
        onUsernameChange = {
            username = it
            error = null
            message = null
        },
        message = message,
        error = error,
        loading = loading,
        onBack = onBack,
        onSubmit = {
            loading = true
            error = null
            message = null
            scope.launch {
                val result = auth.requestPasswordReset(username)
                loading = false
                result.fold(
                    onSuccess = {
                        message =
                            "Listo. En producción enviaríamos un enlace a tu correo."
                    },
                    onFailure = { error = it.message }
                )
            }
        }
    )
}

@Composable
private fun ForgotPasswordContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    message: String?,
    error: String?,
    loading: Boolean,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
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
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = "Flujo mock (sin correo real). Si la cuenta existe, verás confirmación.",
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
            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            message?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = CseSecondary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            PrimaryFlatButton(
                text = if (loading) "…" else "Enviar instrucciones",
                onClick = onSubmit,
                enabled = !loading
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    ControlSelfEDUTheme {
        ForgotPasswordContent(
            username = "",
            onUsernameChange = {},
            message = null,
            error = null,
            loading = false,
            onBack = {},
            onSubmit = {}
        )
    }
}
