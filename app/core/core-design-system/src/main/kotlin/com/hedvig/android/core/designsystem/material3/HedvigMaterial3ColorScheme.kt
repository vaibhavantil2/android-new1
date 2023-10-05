@file:Suppress("UnusedReceiverParameter")

package com.hedvig.android.core.designsystem.material3

import android.annotation.SuppressLint
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.hedvig.android.core.designsystem.component.tokens.HedvigColorSchemeKeyTokens
import com.hedvig.android.core.designsystem.newtheme.hedvigTonalPalette

@SuppressLint("ComposeCompositionLocalUsage")
internal val LocalHedvigMaterial3ColorScheme = staticCompositionLocalOf<HedvigMaterial3ColorScheme> {
  lightHedvigColorScheme(hedvigTonalPalette, lightColorScheme(hedvigTonalPalette))
}

class HedvigMaterial3ColorScheme(
  val containedButtonContainer: Color,
  val onContainedButtonContainer: Color,

  val warningElement: Color,
  val onWarningElement: Color,
  val warningContainer: Color,
  val onWarningContainer: Color,

  // Type refers to "typing" aka when the member is typing something in a text-field. Naming has room for improvement.
  val typeElement: Color,
  val onTypeElement: Color,
  val typeContainer: Color,
  val onTypeContainer: Color,

  // Information related content. Like information cards and so on.
  val infoElement: Color,
  val onInfoElement: Color,
  val infoContainer: Color,
  val onInfoContainer: Color,
)

val ColorScheme.containedButtonContainer: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.containedButtonContainer

val ColorScheme.onContainedButtonContainer: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.onContainedButtonContainer

// region warning colors
val ColorScheme.warningElement: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.warningElement

val ColorScheme.onWarningElement: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.onWarningElement

val ColorScheme.warningContainer: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.warningContainer

val ColorScheme.onWarningContainer: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.onWarningContainer
// endregion

// region typing colors
val ColorScheme.typeElement: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.typeElement

val ColorScheme.onTypeElement: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.onTypeElement

val ColorScheme.typeContainer: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.typeContainer

val ColorScheme.onTypeContainer: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.onTypeContainer
// endregion

// region info colors
val ColorScheme.infoElement: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.infoElement

val ColorScheme.onInfoElement: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.onInfoElement

val ColorScheme.infoContainer: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.infoContainer

val ColorScheme.onInfoContainer: Color
  @ReadOnlyComposable
  @Composable
  get() = LocalHedvigMaterial3ColorScheme.current.onInfoContainer
// endregion

/**
 * Helper function for component color tokens. Here is an example on how to use component color
 * tokens:
 * `MaterialTheme.colorScheme.fromToken(HedvigColorSchemeKeyTokens.Background)`
 */
internal fun HedvigMaterial3ColorScheme.fromToken(value: HedvigColorSchemeKeyTokens, m3Fallback: ColorScheme): Color {
  return when (value) {
    HedvigColorSchemeKeyTokens.Background -> m3Fallback.background
    HedvigColorSchemeKeyTokens.Error -> m3Fallback.error
    HedvigColorSchemeKeyTokens.ErrorContainer -> m3Fallback.errorContainer
    HedvigColorSchemeKeyTokens.InverseOnSurface -> m3Fallback.inverseOnSurface
    HedvigColorSchemeKeyTokens.InversePrimary -> m3Fallback.inversePrimary
    HedvigColorSchemeKeyTokens.InverseSurface -> m3Fallback.inverseSurface
    HedvigColorSchemeKeyTokens.OnBackground -> m3Fallback.onBackground
    HedvigColorSchemeKeyTokens.OnError -> m3Fallback.onError
    HedvigColorSchemeKeyTokens.OnErrorContainer -> m3Fallback.onErrorContainer
    HedvigColorSchemeKeyTokens.OnPrimary -> m3Fallback.onPrimary
    HedvigColorSchemeKeyTokens.OnPrimaryContainer -> m3Fallback.onPrimaryContainer
    HedvigColorSchemeKeyTokens.OnSecondary -> m3Fallback.onSecondary
    HedvigColorSchemeKeyTokens.OnSecondaryContainer -> m3Fallback.onSecondaryContainer
    HedvigColorSchemeKeyTokens.OnSurface -> m3Fallback.onSurface
    HedvigColorSchemeKeyTokens.OnSurfaceVariant -> m3Fallback.onSurfaceVariant
    HedvigColorSchemeKeyTokens.SurfaceTint -> m3Fallback.surfaceTint
    HedvigColorSchemeKeyTokens.OnTertiary -> m3Fallback.onTertiary
    HedvigColorSchemeKeyTokens.OnTertiaryContainer -> m3Fallback.onTertiaryContainer
    HedvigColorSchemeKeyTokens.Outline -> m3Fallback.outline
    HedvigColorSchemeKeyTokens.OutlineVariant -> m3Fallback.outlineVariant
    HedvigColorSchemeKeyTokens.Primary -> m3Fallback.primary
    HedvigColorSchemeKeyTokens.PrimaryContainer -> m3Fallback.primaryContainer
    HedvigColorSchemeKeyTokens.Scrim -> m3Fallback.scrim
    HedvigColorSchemeKeyTokens.Secondary -> m3Fallback.secondary
    HedvigColorSchemeKeyTokens.SecondaryContainer -> m3Fallback.secondaryContainer
    HedvigColorSchemeKeyTokens.Surface -> m3Fallback.surface
    HedvigColorSchemeKeyTokens.SurfaceVariant -> m3Fallback.surfaceVariant
    HedvigColorSchemeKeyTokens.Tertiary -> m3Fallback.tertiary
    HedvigColorSchemeKeyTokens.TertiaryContainer -> m3Fallback.tertiaryContainer

    HedvigColorSchemeKeyTokens.WarningContainer -> warningContainer
    HedvigColorSchemeKeyTokens.OnWarningContainer -> onWarningContainer

    HedvigColorSchemeKeyTokens.TypeElement -> typeElement
    HedvigColorSchemeKeyTokens.OnTypeElement -> onTypeElement
    HedvigColorSchemeKeyTokens.TypeContainer -> typeContainer
    HedvigColorSchemeKeyTokens.OnTypeContainer -> onTypeContainer
  }
}

/** Converts a color token key to the local color scheme provided by the theme */
@ReadOnlyComposable
@Composable
internal fun HedvigColorSchemeKeyTokens.toColor(): Color {
  return LocalHedvigMaterial3ColorScheme.current.fromToken(this, MaterialTheme.colorScheme)
}

/**
 * A low level of alpha used to represent disabled components, such as text in a disabled Button.
 */
const val DisabledAlpha = 0.38f
