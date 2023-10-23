package dev.prince.securify.ui.generate

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import dev.prince.securify.ui.composables.BottomSheetSurface
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.LightBlue
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.LocalSnackbar
import dev.prince.securify.util.clickWithRipple

@Destination
@Composable
fun GenerateScreen(
    viewModel: GenerateViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val interactionSource = MutableInteractionSource()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    val snackbar = LocalSnackbar.current
    LaunchedEffect(Unit) {
        viewModel.messages.collect {
            snackbar(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BgBlack),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(
                top = 18.dp, bottom = 12.dp
            ),
            text = "Password Generator",
            color = Color.White,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFamily
            )
        )

        Surface(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .height(140.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            contentColor = Color.White

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    text = viewModel.password,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        BottomSheetSurface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Options",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Length",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppinsFamily
                        ),
                        color = Color.Black
                    )
                    CustomSlider(
                        value = viewModel.passwordLength.toFloat(),
                        onValueChange = { viewModel.passwordLength = it.toInt() },
                        valueRange = 6f..15f,
                        interactionSource = interactionSource
                    )
                }

                SelectionRow(
                    text = "Lower case",
                    checked = viewModel.lowerCase,
                    onCheckedChange = { viewModel.lowerCase = it }
                )

                SelectionRow(
                    text = "Upper case",
                    checked = viewModel.upperCase,
                    onCheckedChange = { viewModel.upperCase = it }
                )

                SelectionRow(
                    text = "Digits",
                    checked = viewModel.digits,
                    onCheckedChange = { viewModel.digits = it }
                )

                SelectionRow(
                    text = "Special characters",
                    checked = viewModel.specialCharacters,
                    onCheckedChange = { viewModel.specialCharacters = it }
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .padding(
                            start = 22.dp,
                            end = 22.dp,
                            bottom = 32.dp
                        )
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .shadow(10.dp)
                            .background(color = BgBlack)
                            .clickWithRipple {
                                viewModel.checkToggleAndSave()
                            },
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(all = 8.dp)
                                .size(40.dp),
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .padding(
                                start = 16.dp
                            ),
                        onClick = {
                            clipboardManager.setText(
                                AnnotatedString((viewModel.password))
                            )
                            viewModel.showCopyMsg()
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Copy Password",
                            fontSize = 16.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    BackHandler {
        (context as ComponentActivity).finish()
    }
}

@Composable
private fun SelectionRow(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium
            ),
            color = Color.Black,
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            modifier = Modifier.size(42.dp),
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Blue,
                uncheckedTrackColor = LightBlue,
                uncheckedBorderColor = LightBlue,
                uncheckedThumbColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GenerateScreenPreview() {
    GenerateScreen()
}
