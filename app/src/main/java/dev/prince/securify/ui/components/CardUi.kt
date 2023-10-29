package dev.prince.securify.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.prince.securify.R
import dev.prince.securify.ui.add_card.AddCardViewModel
import dev.prince.securify.ui.theme.LightGray
import dev.prince.securify.ui.theme.poppinsFamily

@Composable
fun CardUi(
    cardHolderName: String,
    cardNumber: String,
    cardExpiryDate: String,
    cardCVV: String,
    cardIcon: Int,
    viewModel: AddCardViewModel = hiltViewModel()
) {

    val formattedCardNumber = viewModel.formatCardNumber(cardNumber)
    val formattedExpiryDate = viewModel.formatExpiryDate(cardExpiryDate)

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .height(190.dp)
                .width(320.dp),
            shape = RoundedCornerShape(10.dp),
            contentColor = Color.White,
            shadowElevation = 26.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF6C72CB), Color(0xFF0078FF))
                        )
                    )
            ) {

                Text(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(all = 16.dp),
                    text = cardHolderName,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFamily
                    )
                )

                Image(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(80.dp)
                        .padding(16.dp),
                    painter = painterResource(cardIcon),
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 16.dp),
                    text = formattedCardNumber,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFamily
                    )
                )

                Row(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(end = 16.dp)
                    ) {
                        Text(
                            text = "VALID",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = poppinsFamily,
                                color = LightGray
                            )
                        )
                        Text(
                            text = formattedExpiryDate,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = poppinsFamily,
                                color = Color.White
                            )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(end = 16.dp)
                    ) {
                        Text(
                            text = "CVV",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = poppinsFamily,
                                color = LightGray
                            )
                        )
                        Text(
                            text = cardCVV,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = poppinsFamily,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreditCardPreview() {
    CardUi(
        cardHolderName = "John Doe",
        cardNumber = "4444 3333 2222 1111",
        cardCVV = "415",
        cardExpiryDate = "05/12",
        cardIcon = R.drawable.icon_card
    )
}
*/
