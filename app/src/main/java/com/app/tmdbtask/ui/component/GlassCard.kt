package com.app.tmdbtask.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.tmdbtask.ui.theme.borderColor

@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    blurRadius: Dp = 40.dp,
    backgroundAlpha: Float = 0.80f,
    borderAlpha: Float = 0.4f,
    borderWidth: Dp = 1.dp,
    content: @Composable () -> Unit
) {

    Box(modifier = modifier.clip(shape)) {
        // Background layer with blur
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(radius = blurRadius)
                .background(brush = createGlassGradient(MaterialTheme.colorScheme.surfaceContainer, backgroundAlpha), shape = shape)
                .border(width = borderWidth, brush = createGlassBorderGradient(borderColor, borderAlpha), shape = shape)
        )

        // Content layer stays sharp
        content()
    }
}

private fun createGlassGradient(baseColor: Color, alpha: Float): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            baseColor.copy(alpha = alpha * 1.1f),  // Slightly brighter at top
            baseColor.copy(alpha = alpha),         // Base opacity
            baseColor.copy(alpha = alpha)          // Consistent bottom
        )
    )
}

private fun createGlassBorderGradient(borderColor: Color, alpha: Float): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            borderColor.copy(alpha = alpha * 1.5f),  // Strong highlight
            borderColor.copy(alpha = alpha * 0.3f),  // Fade in middle
            borderColor.copy(alpha = alpha * 0.8f)   // Subtle bottom
        )
    )
}