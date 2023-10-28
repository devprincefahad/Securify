package dev.prince.securify.ui.components.fab

import androidx.compose.ui.graphics.Color
import dev.prince.securify.ui.theme.Blue

interface FabButtonSub {
    val iconTint: Color
    val backgroundTint: Color
}

/**
 * Implementation of [FabButtonSub] interface.
 *
 * @property iconTint The [Color] used to tint the icon of the sub-item.
 * @property backgroundTint The [Color] used to tint the background of the sub-item.
 */
private class FabButtonSubImpl(
    override val iconTint: Color,
    override val backgroundTint: Color
) : FabButtonSub

/**
 * Creates a new instance of [FabButtonSub] with the provided icon and background tints.
 *
 * @param backgroundTint The [Color] used to tint the background of the sub-item.
 * @param iconTint The [Color] used to tint the icon of the sub-item.
 * @return A new instance of [FabButtonSub] with the specified icon and background tints.
 */
fun FabButtonSub(
    backgroundTint: Color = Blue,
    iconTint: Color = Color.White
): FabButtonSub = FabButtonSubImpl(iconTint, backgroundTint)