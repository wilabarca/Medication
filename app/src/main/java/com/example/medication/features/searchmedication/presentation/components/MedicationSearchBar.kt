package com.example.medication.features.searchmedication.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Paleta búsqueda médica ─────────────────────────────────────────────────────
private val SbTeal        = Color(0xFF00897B)   // Teal clínico
private val SbTealLight   = Color(0xFFE0F2F1)
private val SbTealDark    = Color(0xFF00695C)
private val SbBorder      = Color(0xFFB2DFDB)
private val SbBorderFocus = Color(0xFF00897B)
private val SbTextPrimary = Color(0xFF0D1F2D)
private val SbTextMuted   = Color(0xFF78909C)

@Composable
fun MedicineSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    // ── Elevación animada al enfocar ───────────────────────────────────────────
    val elevation by animateDpAsState(
        targetValue = if (isFocused || query.isNotEmpty()) 10.dp else 2.dp,
        animationSpec = tween(300),
        label = "searchbar_elevation"
    )

    // ── Pulso en el ícono lupa ─────────────────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "icon_pulse")
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue  = 1.20f,
        animationSpec = infiniteRepeatable(
            animation  = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_scale"
    )

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation    = elevation,
                    shape        = RoundedCornerShape(16.dp),
                    ambientColor = SbTeal.copy(alpha = 0.22f),
                    spotColor    = SbTeal.copy(alpha = 0.22f)
                )
                .onFocusChanged { isFocused = it.isFocused },
            placeholder = {
                Text(
                    "Buscar medicamento, principio activo...",
                    color    = SbTextMuted,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = if (isFocused || query.isNotEmpty()) SbTeal else SbTextMuted,
                    modifier = Modifier
                        .size(22.dp)
                        .graphicsLayer {
                            scaleX = if (isFocused) iconScale else 1f
                            scaleY = if (isFocused) iconScale else 1f
                        }
                )
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = fadeIn(tween(200)) + scaleIn(tween(200)),
                    exit  = fadeOut(tween(150)) + scaleOut(tween(150))
                ) {
                    IconButton(onClick = {
                        onClear()
                        focusManager.clearFocus()
                    }) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(SbTealLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Limpiar",
                                tint     = SbTeal,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            },
            singleLine = true,
            shape  = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor      = SbBorderFocus,
                unfocusedBorderColor    = SbBorder,
                focusedContainerColor   = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor             = SbTeal,
                focusedTextColor        = SbTextPrimary,
                unfocusedTextColor      = SbTextPrimary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
        )

        // ── Chips de sugerencias rápidas (visibles al enfocar sin texto) ────────
        AnimatedVisibility(
            visible = isFocused && query.isEmpty(),
            enter   = fadeIn(tween(250)) + expandVertically(tween(250)),
            exit    = fadeOut(tween(180)) + shrinkVertically(tween(180))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Analgésicos", "Antibióticos", "Vitaminas").forEach { label ->
                    SuggestionChip(
                        onClick = { onQueryChanged(label) },
                        label   = {
                            Text(
                                label,
                                fontSize   = 11.sp,
                                color      = SbTealDark,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = SbTealLight
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled     = true,
                            borderColor = SbBorder
                        )
                    )
                }
            }
        }
    }
}