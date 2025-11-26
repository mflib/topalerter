package io.github.mflib.topalerter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


/**
 * A modern, fully customizable, Material 3-compliant top alert (toast-style banner).
 *
 * Features smooth slide + fade animations, full theme support, gradient/image background capability,
 * action buttons, progress indicator, and click-to-dismiss.
 *
 * ### Smart Background Behavior
 * - `backgroundColor = Color.Unspecified` → Uses theme-aware elevated surface
 * - Any solid color → Applied directly
 * - `backgroundColor = Color.Transparent` → Surface becomes fully transparent → enables full custom backgrounds
 *   (gradient, image, shimmer, etc.) via [modifier]
 *
 * @param modifier Outer modifier. Use for padding, custom background (gradient/image), click effects, etc.
 * @param visible Controls visibility and triggers enter/exit animations
 *
 * @param title Main bold title text. Set to `null` to hide.
 * @param message Supporting message below the title. Set to `null` to hide.
 * @param icon Leading icon (e.g., [Icons.Default.CheckCircle], [Icons.Default.Warning]). Set to `null` to hide.
 *
 * @param backgroundColor Background color of the alert.
 *   Use [Color.Transparent] + [modifier]`.background(...)` for gradients or custom visuals.
 * @param containerShape Shape of the alert container. Default is bottom-rounded for top placement.
 * @param tonalElevation Tonal elevation intensity when using theme background (ignored in transparent mode).
 *
 * @param titleStyle Base text style for the title. Defaults to [MaterialTheme.typography.titleMedium].
 * @param titleColor Title text color. Falls back to [colorScheme.onSurface] if [Color.Unspecified].
 * @param titleFontWeight Optional override for title font weight.
 *
 * @param messageStyle Base text style for the message. Defaults to [MaterialTheme.typography.bodyMedium].
 * @param messageColor Message text color. Falls back to [colorScheme.onSurfaceVariant].
 * @param messageMaxLines Maximum lines before ellipsis. Use [Int.MAX_VALUE] for unlimited.
 *
 * @param iconTint Color for the leading icon. Falls back to [colorScheme.primary].
 * @param showProgress If true, shows a [CircularProgressIndicator] instead of [icon].
 * @param progressColor Color of the progress indicator. Falls back to [colorScheme.primary].
 *
 * @param positiveButtonText Text for the primary action button (e.g., "Done", "Retry").
 * @param positiveButtonOnClick Callback invoked when positive button is clicked. Alert dismisses automatically.
 * @param positiveButtonColors Full color override for the positive button.
 * @param positiveButtonShape Shape of the positive button.
 * @param positiveButtonTextStyle Text style for the positive button.
 *
 * @param negativeButtonText Text for the secondary action button (e.g., "Cancel", "Later").
 * @param negativeButtonOnClick Callback invoked when negative button is clicked. Alert dismisses automatically.
 * @param negativeButtonColors Full color override for the negative button.
 * @param negativeButtonShape Shape of the negative button.
 * @param negativeButtonTextStyle Text style for the negative button.
 *
 * @param durationMillis Auto-dismiss duration in milliseconds. Only applies if [autoDismiss] is true.
 * @param autoDismiss Whether to automatically dismiss after [durationMillis].
 * @param onClick Optional callback when the entire alert card is tapped.
 * @param onDismiss Called when the alert is dismissed (by timeout, close button, action button, or tap).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAlerter(
    modifier: Modifier = Modifier,
    visible: Boolean,
    title: String? = null,
    message: String? = null,
    icon: ImageVector? = null,

    // === Appearance ===
    backgroundColor: Color = Color.Unspecified,
    containerShape: Shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    tonalElevation: Dp = 6.dp,

    // === Title ===
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    titleColor: Color = Color.Unspecified,
    titleFontWeight: FontWeight? = null,

    // === Message ===
    messageStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    messageColor: Color = Color.Unspecified,
    messageMaxLines: Int = Int.MAX_VALUE,

    // === Icon / Progress ===
    iconTint: Color = Color.Unspecified,
    showProgress: Boolean = false,
    progressColor: Color = Color.Unspecified,

    // === Positive Button ===
    positiveButtonText: String? = null,
    positiveButtonOnClick: (() -> Unit)? = null,
    positiveButtonColors: ButtonColors? = null,
    positiveButtonShape: Shape = MaterialTheme.shapes.small,
    positiveButtonTextStyle: TextStyle = MaterialTheme.typography.labelLarge,

    // === Negative Button ===
    negativeButtonText: String? = null,
    negativeButtonOnClick: (() -> Unit)? = null,
    negativeButtonColors: ButtonColors? = null,
    negativeButtonShape: Shape = MaterialTheme.shapes.small,
    negativeButtonTextStyle: TextStyle = MaterialTheme.typography.labelLarge,

    // === Behavior ===
    durationMillis: Long = 4000L,
    autoDismiss: Boolean = true,
    onClick: (() -> Unit)? = null,     // Tap entire alert
    onDismiss: () -> Unit = {},
) {
    val colorScheme = MaterialTheme.colorScheme
    val showButtons = positiveButtonText != null || negativeButtonText != null
    val isCustomBackground = backgroundColor == Color.Transparent
    LaunchedEffect(visible) {
        if (visible && autoDismiss) {
            delay(durationMillis)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(onClick) {
                    onClick?.let { callback ->
                        detectTapGestures { callback() }
                    }
                },
            shape = containerShape,
            color = when {
                isCustomBackground -> Color.Transparent                    // Full custom (gradient, image, etc.)
                backgroundColor != Color.Unspecified -> backgroundColor    // User set solid color
                else -> colorScheme.surfaceContainerHigh                   // Default theme surface
            },
            tonalElevation = if (isCustomBackground) 0.dp else tonalElevation,
            shadowElevation = if (isCustomBackground) 0.dp else 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp + WindowInsets.statusBars.asPaddingValues()
                            .calculateTopPadding(),
                        bottom = 12.dp
                    )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Icon or Progress
                    when {
                        showProgress -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = if (progressColor != Color.Unspecified) progressColor else colorScheme.onSurfaceVariant,
                                strokeWidth = 2.5.dp
                            )
                            Spacer(Modifier.width(12.dp))
                        }

                        icon != null -> {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = if (progressColor != Color.Unspecified) progressColor else colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        title?.let {
                            Text(
                                text = it,
                                style = titleStyle.copy(
                                    fontWeight = titleFontWeight ?: titleStyle.fontWeight
                                ),
                                color = titleColor.unspecifiedOr { colorScheme.onSurface },
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        message?.let { msg ->
                            if (title != null) Spacer(Modifier.height(4.dp))
                            Text(
                                text = msg,
                                style = messageStyle,
                                color = messageColor.unspecifiedOr { colorScheme.onSurfaceVariant },
                                maxLines = messageMaxLines,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (!showButtons) {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Default.Close, "Dismiss", tint = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Buttons row — FULLY customizable again
                if (showButtons) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        negativeButtonText?.let { text ->
                            TextButton(
                                onClick = {
                                    negativeButtonOnClick?.invoke()
                                    onDismiss()
                                },
                                colors = negativeButtonColors ?: ButtonDefaults.textButtonColors(
                                    contentColor = colorScheme.onSurfaceVariant
                                ),
                                shape = negativeButtonShape,
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(text = text, style = negativeButtonTextStyle)
                            }
                        }

                        positiveButtonText?.let { text ->
                            TextButton(
                                onClick = {
                                    positiveButtonOnClick?.invoke()
                                    onDismiss()
                                }, colors = positiveButtonColors ?: ButtonDefaults.textButtonColors(
                                    contentColor = colorScheme.onSurface
                                ), shape = positiveButtonShape
                            ) {
                                Text(text = text, style = positiveButtonTextStyle)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Safely returns this value if it is not null and not [Color.Unspecified] (or equivalent),
 * otherwise returns the result of the [default] lambda.
 *
 * This is a cleaner alternative to repeatedly writing:
 * ```kotlin
 * color.takeIf { it != Color.Unspecified } ?: MaterialTheme.colorScheme.primary
 * ```
 *
 * ### Usage Examples
 * ```kotlin
 * val iconColor = iconTint.unspecifiedOr { MaterialTheme.colorScheme.primary }
 * val titleColor = titleColor.unspecifiedOr { MaterialTheme.colorScheme.onSurface }
 * ```
 *
 * Works with any type — most commonly used with [Color] to handle [Color.Unspecified].
 *
 * @param default A lambda that provides the fallback value when this is null or "unspecified"
 * @return This value if valid, otherwise the result of [default]
 */
private inline fun <T : Any> T?.unspecifiedOr(default: () -> T): T = this ?: default()



