package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private fun getPaletteColorScheme(palette: String, darkTheme: Boolean): androidx.compose.material3.ColorScheme {
    return when (palette.lowercase()) {
        "ocean" -> if (darkTheme) {
            darkColorScheme(
                primary = OceanDarkPrimary,
                onPrimary = Color.Black,
                primaryContainer = OceanPrimary,
                onPrimaryContainer = Color.White,
                secondary = CatPersonal,
                onSecondary = Color.White,
                secondaryContainer = OceanDarkSurface,
                onSecondaryContainer = DarkInk,
                background = OceanDarkBg,
                onBackground = DarkInk,
                surface = OceanDarkSurface,
                onSurface = DarkInk,
                surfaceVariant = DarkSurfaceVariant,
                onSurfaceVariant = DarkMuted,
                outline = DarkBorderLine,
                outlineVariant = DarkBorderLine
            )
        } else {
            lightColorScheme(
                primary = OceanPrimary,
                onPrimary = Color.White,
                primaryContainer = OceanContainer,
                onPrimaryContainer = OceanPrimary,
                secondary = CatPersonal,
                onSecondary = Color.White,
                secondaryContainer = OceanContainer,
                onSecondaryContainer = CharcoalInk,
                background = OceanBg,
                onBackground = CharcoalInk,
                surface = OceanSurface,
                onSurface = CharcoalInk,
                surfaceVariant = SurfaceVariantWarm,
                onSurfaceVariant = MutedText,
                outline = BorderLine,
                outlineVariant = BorderLine
            )
        }
        "sunset" -> if (darkTheme) {
            darkColorScheme(
                primary = SunsetDarkPrimary,
                onPrimary = Color.Black,
                primaryContainer = SunsetPrimary,
                onPrimaryContainer = Color.White,
                secondary = CatHome,
                onSecondary = Color.White,
                secondaryContainer = SunsetDarkSurface,
                onSecondaryContainer = DarkInk,
                background = SunsetDarkBg,
                onBackground = DarkInk,
                surface = SunsetDarkSurface,
                onSurface = DarkInk,
                surfaceVariant = DarkSurfaceVariant,
                onSurfaceVariant = DarkMuted,
                outline = DarkBorderLine,
                outlineVariant = DarkBorderLine
            )
        } else {
            lightColorScheme(
                primary = SunsetPrimary,
                onPrimary = Color.White,
                primaryContainer = SunsetContainer,
                onPrimaryContainer = SunsetPrimary,
                secondary = CatHome,
                onSecondary = Color.White,
                secondaryContainer = SunsetContainer,
                onSecondaryContainer = CharcoalInk,
                background = SunsetBg,
                onBackground = CharcoalInk,
                surface = SunsetSurface,
                onSurface = CharcoalInk,
                surfaceVariant = SurfaceVariantWarm,
                onSurfaceVariant = MutedText,
                outline = BorderLine,
                outlineVariant = BorderLine
            )
        }
        "purple" -> if (darkTheme) {
            darkColorScheme(
                primary = PurpleDarkPrimary,
                onPrimary = Color.Black,
                primaryContainer = PurplePrimary,
                onPrimaryContainer = Color.White,
                secondary = CatStudy,
                onSecondary = Color.White,
                secondaryContainer = PurpleDarkSurface,
                onSecondaryContainer = DarkInk,
                background = PurpleDarkBg,
                onBackground = DarkInk,
                surface = PurpleDarkSurface,
                onSurface = DarkInk,
                surfaceVariant = DarkSurfaceVariant,
                onSurfaceVariant = DarkMuted,
                outline = DarkBorderLine,
                outlineVariant = DarkBorderLine
            )
        } else {
            lightColorScheme(
                primary = PurplePrimary,
                onPrimary = Color.White,
                primaryContainer = PurpleContainer,
                onPrimaryContainer = PurplePrimary,
                secondary = CatStudy,
                onSecondary = Color.White,
                secondaryContainer = PurpleContainer,
                onSecondaryContainer = CharcoalInk,
                background = PurpleBg,
                onBackground = CharcoalInk,
                surface = PurpleSurface,
                onSurface = CharcoalInk,
                surfaceVariant = SurfaceVariantWarm,
                onSurfaceVariant = MutedText,
                outline = BorderLine,
                outlineVariant = BorderLine
            )
        }
        "midnight" -> darkColorScheme(
            primary = MidnightPrimary,
            onPrimary = Color.Black,
            primaryContainer = MidnightContainer,
            onPrimaryContainer = Color.White,
            secondary = CatTravel,
            onSecondary = Color.White,
            secondaryContainer = MidnightSurface,
            onSecondaryContainer = DarkInk,
            background = MidnightBg,
            onBackground = DarkInk,
            surface = MidnightSurface,
            onSurface = DarkInk,
            surfaceVariant = MidnightContainer,
            onSurfaceVariant = DarkMuted,
            outline = DarkBorderLine,
            outlineVariant = DarkBorderLine
        )
        else -> { // "forest" (Default)
            if (darkTheme) {
                darkColorScheme(
                    primary = SoftSage,
                    onPrimary = ForestGreenDark,
                    primaryContainer = ForestGreen,
                    onPrimaryContainer = Color.White,
                    secondary = CatWork,
                    onSecondary = Color.White,
                    secondaryContainer = DarkSurfaceVariant,
                    onSecondaryContainer = DarkInk,
                    background = DarkPaperBg,
                    onBackground = DarkInk,
                    surface = DarkSurface,
                    onSurface = DarkInk,
                    surfaceVariant = DarkSurfaceVariant,
                    onSurfaceVariant = DarkMuted,
                    outline = DarkBorderLine,
                    outlineVariant = DarkBorderLine
                )
            } else {
                lightColorScheme(
                    primary = ForestGreen,
                    onPrimary = Color.White,
                    primaryContainer = SoftSage,
                    onPrimaryContainer = ForestGreenDark,
                    secondary = CatWork,
                    onSecondary = Color.White,
                    secondaryContainer = CatWorkBg,
                    onSecondaryContainer = CharcoalInk,
                    background = PaperBg,
                    onBackground = CharcoalInk,
                    surface = SurfaceWhite,
                    onSurface = CharcoalInk,
                    surfaceVariant = SurfaceVariantWarm,
                    onSurfaceVariant = MutedText,
                    outline = BorderLine,
                    outlineVariant = BorderLine
                )
            }
        }
    }
}

@Composable
fun LifeOSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themePalette: String = "forest",
    pageAccent: String? = null,
    content: @Composable () -> Unit
) {
    val baseScheme = getPaletteColorScheme(themePalette, darkTheme)
    
    val colorScheme = if (!pageAccent.isNullOrBlank()) {
        val customPrimary = when (pageAccent.lowercase()) {
            "ocean" -> OceanPrimary
            "sunset" -> SunsetPrimary
            "purple" -> PurplePrimary
            "forest" -> ForestGreen
            "emerald" -> MidnightPrimary
            else -> baseScheme.primary
        }
        baseScheme.copy(
            primary = customPrimary,
            primaryContainer = customPrimary.copy(alpha = 0.15f)
        )
    } else {
        baseScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
