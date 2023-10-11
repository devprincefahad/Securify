package dev.prince.securify.ui.generate

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.prince.securify.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    activeTrackColor: Color = Blue,
    inactiveTrackColor: Color = Color.DarkGray,
    thumbTextColor: Color = Color.Black, // Changed to black
    interactionSource: MutableInteractionSource
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        colors = SliderDefaults.colors(
            activeTickColor = activeTrackColor,
            activeTrackColor = activeTrackColor,
            inactiveTrackColor = inactiveTrackColor,
            thumbColor = Color.Transparent // Set thumb color to transparent
        ),
        thumb = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Blue, shape = CircleShape)
                    .padding(6.dp), // Add padding to create the border
                contentAlignment = Alignment.Center,
                content = {
                    Box(
                        modifier = Modifier
                            .size(32.dp) // Adjust the size of the inner white circle
                            .background(color = Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                        content = {
                            Text(
                                text = value.toInt().toString(),
                                color = thumbTextColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    )
                }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        interactionSource = interactionSource,
        onValueChangeFinished = null
    )
}