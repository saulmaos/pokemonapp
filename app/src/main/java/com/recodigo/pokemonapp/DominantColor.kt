package com.recodigo.pokemonapp

import android.content.Context
import androidx.collection.LruCache
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberDominantColorState(
    context: Context = LocalContext.current,
    defaultColor: Color = MaterialTheme.colors.surface,
    defaultOnColor: Color = MaterialTheme.colors.onSurface,
    cache: LruCache<String, DominantColors>
): DominantColorState = remember {
    DominantColorState(context, defaultColor, defaultOnColor, cache)
}

class DominantColorState(
    private val context: Context,
    private val defaultColor: Color,
    private val defaultOnColor: Color,
    private val cache: LruCache<String, DominantColors>
) {

    var color by mutableStateOf(defaultColor)
        private set
    var onColor by mutableStateOf(defaultOnColor)
        private set

    suspend fun updateColorsFromImageUrl(url: String) {
        val result = calculateDominantColor(url)
        color = result?.color ?: defaultColor
        onColor = result?.onColor ?: defaultOnColor
    }

    private suspend fun calculateDominantColor(url: String): DominantColors? {
        val cached = cache.get(url)
        if (cached != null) return cached

        return calculateDominantSwatchInImage(context, url)
            ?.let { swatch ->
                DominantColors(
                    color = Color(swatch.rgb),
                    onColor = Color(swatch.titleTextColor)
                )
            }
            ?.also { colors -> cache.put(url, colors) }
    }

    private suspend fun calculateDominantSwatchInImage(
        context: Context,
        imageUrl: String
    ): Palette.Swatch? {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .size(50)
            .allowHardware(false)
            .build()

        val bitmap = when (val result = Coil.execute(request)) {
            is SuccessResult -> result.drawable.toBitmap()
            else -> null
        }

        return bitmap?.let {
            withContext(Dispatchers.Default) {
                val palette = Palette.Builder(bitmap)
                    .addFilter { rgb, hsl ->
                        val luminance = ColorUtils.calculateLuminance(rgb)
                        !(luminance > 0.7 || luminance < 0.25)
                    }
                    .maximumColorCount(8)
                    .generate()
                palette.dominantSwatch
            }
        }
    }
}

data class DominantColors(val color: Color, val onColor: Color)