package com.controlself.edu.ui.screens.protection

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface
import com.controlself.edu.ui.theme.CseWarning
import com.controlself.edu.ui.theme.CseWhite
import kotlinx.coroutines.launch

@Composable
fun AdminProtectionScreen(onBack: () -> Unit) {
    val container = LocalAppContainer.current
    val adminRepo = container.adminPasswordRepository
    val gateway = container.deviceAdminGateway
    val scope = rememberCoroutineScope()

    val passwordSet by adminRepo.observeIsSet()
        .collectAsStateWithLifecycle(initialValue = false)
    var adminActive by remember { mutableStateOf(gateway.isAdminActive()) }
    val deviceOwner = remember { gateway.isDeviceOwner() }

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var showDisableDialog by remember { mutableStateOf(false) }
    var disablePassword by remember { mutableStateOf("") }

    val enableLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        adminActive = gateway.isAdminActive()
        message = if (adminActive || result.resultCode == Activity.RESULT_OK) {
            if (adminActive) "Device Admin activado." else "Device Admin no se activó."
        } else {
            "Device Admin no se activó."
        }
    }

    if (showDisableDialog) {
        AlertDialog(
            onDismissRequest = { showDisableDialog = false },
            title = { Text("Desactivar protección") },
            text = {
                Column {
                    Text("Ingresa la contraseña de administrador.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = disablePassword,
                        onValueChange = { disablePassword = it },
                        label = { Text("Contraseña admin") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            if (!adminRepo.verify(disablePassword)) {
                                error = "Contraseña incorrecta"
                                showDisableDialog = false
                                disablePassword = ""
                                return@launch
                            }
                            val ok = gateway.disableAdmin()
                            adminActive = gateway.isAdminActive()
                            showDisableDialog = false
                            disablePassword = ""
                            error = null
                            message = if (ok || !adminActive) {
                                "Device Admin desactivado."
                            } else {
                                "No se pudo desactivar Device Admin."
                            }
                        }
                    }
                ) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDisableDialog = false
                    disablePassword = ""
                }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Protección anti-desinstalación",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            StatusCard(
                passwordSet = passwordSet,
                adminActive = adminActive,
                deviceOwner = deviceOwner
            )
            Spacer(modifier = Modifier.height(16.dp))
            LimitsCard()
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = CseWhite,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (passwordSet) "Cambiar contraseña admin" else "Definir contraseña admin",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mínimo 6 caracteres. Solo padre/docente. Nunca se muestra al estudiante.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CseMuted
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (passwordSet) {
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Contraseña actual") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nueva contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            error = null
                            message = null
                            if (newPassword != confirmPassword) {
                                error = "Las contraseñas no coinciden"
                                return@Button
                            }
                            scope.launch {
                                val result = adminRepo.setPassword(
                                    newPassword = newPassword,
                                    currentPassword = if (passwordSet) currentPassword else null
                                )
                                result.onSuccess {
                                    message = "Contraseña guardada."
                                    newPassword = ""
                                    confirmPassword = ""
                                    currentPassword = ""
                                    error = null
                                }.onFailure {
                                    error = it.message ?: "No se pudo guardar"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar contraseña")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (!adminActive) {
                Button(
                    onClick = {
                        if (!passwordSet) {
                            error = "Define primero la contraseña admin"
                            return@Button
                        }
                        enableLauncher.launch(gateway.createEnableAdminIntent())
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = passwordSet
                ) {
                    Text("Activar Device Admin")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        error = null
                        showDisableDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Desactivar Device Admin")
                }
            }

            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = CseDanger)
            }
            message?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = CseGreen)
            }

            if (deviceOwner) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Este dispositivo es Device Owner: se puede aplicar bloqueo de desinstalación institucional.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CseBlue
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "QA Device Owner (solo desarrollo):\n" +
                        "adb shell dpm set-device-owner " +
                        "com.controlself.edu/.system.admin.ControlSelfDeviceAdminReceiver",
                    style = MaterialTheme.typography.bodySmall,
                    color = CseMuted
                )
            }
        }
    }
}

@Composable
private fun StatusCard(passwordSet: Boolean, adminActive: Boolean, deviceOwner: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Estado", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Contraseña admin: " + if (passwordSet) "definida" else "sin definir",
                color = if (passwordSet) CseGreen else CseWarning
            )
            Text(
                text = "Device Admin: " + if (adminActive) "activo" else "inactivo",
                color = if (adminActive) CseGreen else CseMuted
            )
            Text(
                text = "Device Owner: " + if (deviceOwner) "sí" else "no",
                color = if (deviceOwner) CseBlue else CseMuted
            )
        }
    }
}

@Composable
private fun LimitsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Límites de Android", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "En un teléfono normal, Device Admin dificulta la desinstalación " +
                    "(hay que desactivarlo primero). No puede interceptar el diálogo del launcher " +
                    "con tu contraseña. El bloqueo total requiere Device Owner (dispositivos escolares).",
                style = MaterialTheme.typography.bodyMedium,
                color = CseMuted
            )
        }
    }
}
