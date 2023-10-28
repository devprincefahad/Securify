package dev.prince.securify.ui.components.fab

import dev.prince.securify.R

/**
 * Represents the main floating action button (FAB) with an icon and optional rotation.
 * The main FAB is the primary action button that can be expanded to reveal sub-items.
 */
interface FabButtonMain {
    val iconRes: Int
    val iconRotate: Float?
}

/**
 * Implementation of [FabButtonMain] interface.
 *
 * @property iconRes The [ImageVector] representing the icon to be displayed on the main FAB.
 * @property iconRotate The optional rotation angle for the main FAB icon. If null, the icon will not be rotated.
 */
private class FabButtonMainImpl(
    override val iconRes: Int,
    override val iconRotate: Float?
) : FabButtonMain

/**
 * Creates a new instance of [FabButtonMain] with the provided icon and optional rotation.
 *
 * @param iconRes The [ImageVector] representing the icon to be displayed on the main FAB.
 * @param iconRotate The optional rotation angle for the main FAB icon. If null, the icon will not be rotated.
 * @return A new instance of [FabButtonMain] with the specified icon and rotation.
 */
fun FabButtonMain(iconRes: Int = R.drawable.icon_add, iconRotate: Float = 45f): FabButtonMain =
    FabButtonMainImpl(iconRes, iconRotate)