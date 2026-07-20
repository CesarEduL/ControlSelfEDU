package com.controlself.edu.ui.screens.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.R
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.SecondaryFlatButton
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CsePrimary

/**
 * Welcome minimalista (Stitch a.1): marca + CTAs Empezar / Tengo una cuenta.
 */
@Composable
fun WelcomeScreen(
    onStart: () -> Unit,
    onHaveAccount: () -> Unit
) {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(700))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
            .padding(horizontal = 24.dp)
            .alpha(alpha.value),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.35f))
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo ControlSelf EDU",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "ControlSelf EDU",
            style = MaterialTheme.typography.displayLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Aprende para divertirte",
            style = MaterialTheme.typography.bodyLarge,
            color = CseOnSurfaceVariant,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        PrimaryFlatButton(
            text = "EMPEZAR",
            onClick = onStart,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        SecondaryFlatButton(
            text = "TENGO UNA CUENTA",
            onClick = onHaveAccount,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(0.45f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.VerifiedUser, null, tint = CseOnSurfaceVariant.copy(alpha = 0.55f))
            Icon(Icons.Filled.FamilyRestroom, null, tint = CseOnSurfaceVariant.copy(alpha = 0.55f))
            Icon(Icons.Filled.School, null, tint = CseOnSurfaceVariant.copy(alpha = 0.55f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "© ControlSelf Educational Ecosystem",
            style = MaterialTheme.typography.labelMedium,
            color = CseOnSurfaceVariant.copy(alpha = 0.55f)
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    ControlSelfEDUTheme {
        WelcomeScreen(onStart = {}, onHaveAccount = {})
    }
}
