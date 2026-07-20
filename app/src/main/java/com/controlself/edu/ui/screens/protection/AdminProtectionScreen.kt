package com.controlself.edu.ui.screens.protection

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.SecondaryFlatButton
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseError
import com.controlself.edu.ui.theme.CseErrorContainer
import com.controlself.edu.ui.theme.CseOnSecondaryContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseSurfaceContainer
import com.controlself.edu.ui.theme.CseSurfaceHighest
import com.controlself.edu.ui.theme.CseSurfaceLow
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
    var showPasswordForm by remember { mutableStateOf(false) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = CsePrimary,
        unfocusedBorderColor = CseOutlineVariant,
        focusedContainerColor = CseBackground,
        unfocusedContainerColor = CseBackground
    )

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
            containerColor = CseWhite,
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
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors
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
                ) { Text("Confirmar", color = CsePrimary) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDisableDialog = false
                    disablePassword = ""
                }) { Text("Cancelar", color = CseOnSurfaceVariant) }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = CsePrimary
                )
            }
            Text(
                text = "Protección",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = CsePrimary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.AdminPanelSettings,
                    contentDescription = null,
                    tint = CseSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ACCESO AUTORIZADO",
                    style = MaterialTheme.typography.labelSmall,
                    color = CseSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Protección admin",
                style = MaterialTheme.typography.headlineLarge,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Configura la seguridad para evitar cambios no autorizados al entorno educativo.",
                style = MaterialTheme.typography.bodyMedium,
                color = CseOnSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            SecurityStatusCard(
                passwordSet = passwordSet,
                adminActive = adminActive,
                deviceOwner = deviceOwner
            )

            Spacer(modifier = Modifier.height(12.dp))
            HowItWorksCard()

            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = CseWhite,
                border = BorderStroke(1.dp, CseOutlineVariant),
                shadowElevation = 0.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Contraseña admin",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CsePrimary
                            )
                            Text(
                                text = "Clave maestra para ajustes restringidos.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CseOnSurfaceVariant
                            )
                        }
                        Icon(Icons.Outlined.Key, null, tint = CsePrimary)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (!showPasswordForm) {
                        PrimaryFlatButton(
                            text = if (passwordSet) "Cambiar contraseña" else "Definir contraseña",
                            onClick = { showPasswordForm = true }
                        )
                    } else {
                        if (passwordSet) {
                            OutlinedTextField(
                                value = currentPassword,
                                onValueChange = { currentPassword = it },
                                label = { Text("Contraseña actual") },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = fieldColors
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
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = fieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirmar") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = fieldColors
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PrimaryFlatButton(
                            text = "Guardar contraseña",
                            onClick = {
                                error = null
                                message = null
                                if (newPassword != confirmPassword) {
                                    error = "Las contraseñas no coinciden"
                                    return@PrimaryFlatButton
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
                                        showPasswordForm = false
                                    }.onFailure {
                                        error = it.message ?: "No se pudo guardar"
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SecondaryFlatButton(
                            text = "Cancelar",
                            onClick = {
                                showPasswordForm = false
                                newPassword = ""
                                confirmPassword = ""
                                currentPassword = ""
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = CseSurfaceLow,
                border = BorderStroke(2.dp, CseOutlineVariant),
                shadowElevation = 0.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Device Admin",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CsePrimary
                            )
                            Text(
                                text = "Privilegios de sistema para este dispositivo.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CseOnSurfaceVariant
                            )
                        }
                        Icon(Icons.Outlined.Security, null, tint = CseError)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CseErrorContainer.copy(alpha = 0.35f))
                            .padding(12.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            null,
                            tint = CseError,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Desactivar Device Admin requiere la contraseña maestra y detiene el seguimiento activo.",
                            style = MaterialTheme.typography.labelSmall,
                            color = CseError
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (!adminActive) {
                        PrimaryFlatButton(
                            text = "Activar Device Admin",
                            onClick = {
                                if (!passwordSet) {
                                    error = "Define primero la contraseña admin"
                                    return@PrimaryFlatButton
                                }
                                enableLauncher.launch(gateway.createEnableAdminIntent())
                            },
                            enabled = passwordSet
                        )
                    } else {
                        SecondaryFlatButton(
                            text = "Desactivar Device Admin",
                            onClick = {
                                error = null
                                showDisableDialog = true
                            }
                        )
                    }
                }
            }

            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = CseError)
            }
            message?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = CseSecondary)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CseSurfaceHighest)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Espacio padre y docente",
                        fontWeight = FontWeight.Bold,
                        color = CsePrimary
                    )
                    Text(
                        text = "Los estudiantes no pueden ver ni usar este módulo.",
                        style = MaterialTheme.typography.labelSmall,
                        color = CseOnSurfaceVariant
                    )
                }
            }

            if (deviceOwner) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Device Owner activo: se puede aplicar bloqueo institucional de desinstalación.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CsePrimary
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "QA Device Owner (solo desarrollo):\n" +
                        "adb shell dpm set-device-owner " +
                        "com.controlself.edu/.system.admin.ControlSelfDeviceAdminReceiver",
                    style = MaterialTheme.typography.bodySmall,
                    color = CseOnSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SecurityStatusCard(
    passwordSet: Boolean,
    adminActive: Boolean,
    deviceOwner: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(CsePrimary)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Estado de seguridad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CsePrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                StatusRow(
                    icon = Icons.Outlined.Password,
                    title = "Contraseña admin",
                    subtitle = if (passwordSet) "Definida y cifrada" else "Sin definir",
                    badge = if (passwordSet) "SÍ" else "NO",
                    badgeOk = passwordSet
                )
                Spacer(modifier = Modifier.height(10.dp))
                StatusRow(
                    icon = Icons.Outlined.Security,
                    title = "Device Admin",
                    subtitle = "Protección anti-desinstalación",
                    badge = if (adminActive) "ACTIVO" else "INACTIVO",
                    badgeOk = adminActive
                )
                Spacer(modifier = Modifier.height(10.dp))
                StatusRow(
                    icon = Icons.Outlined.AdminPanelSettings,
                    title = "Device Owner",
                    subtitle = "Modo institucional",
                    badge = if (deviceOwner) "SÍ" else "NO",
                    badgeOk = deviceOwner
                )
            }
        }
    }
}

@Composable
private fun StatusRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    badge: String,
    badgeOk: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CseSurfaceContainer)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = if (badgeOk) CseSecondary else CseWarning)
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, color = CsePrimary)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = CseOnSurfaceVariant)
        }
        Text(
            text = badge,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (badgeOk) CseOnSecondaryContainer else CseError,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    if (badgeOk) CseSecondaryContainer else CseErrorContainer
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun HowItWorksCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(CseSecondary)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Cómo funciona",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CsePrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ControlSelf EDU usa la API de Device Administration de Android " +
                        "para dificultar el cierre forzado o el borrado de datos que eviten los límites de tiempo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CseOnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CseSurfaceLow)
                        .border(1.dp, CseOutlineVariant, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Límites de Android",
                        fontWeight = FontWeight.Bold,
                        color = CsePrimary
                    )
                    Text(
                        text = "En un teléfono normal hay que desactivar Device Admin antes de desinstalar. " +
                            "El bloqueo total requiere Device Owner (dispositivos escolares).",
                        style = MaterialTheme.typography.labelSmall,
                        color = CseOnSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
