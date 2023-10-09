package dev.prince.securify.ui.intro

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.R
import dev.prince.securify.ui.destinations.SetupKeyScreenDestination
import dev.prince.securify.ui.theme.LightBlue
import dev.prince.securify.ui.theme.poppinsFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination
@Composable
fun IntroScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    Box(
        modifier = with(Modifier) {
            fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.surviellance),
                    contentScale = ContentScale.Crop
                )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.intro_tagline_1),
                textAlign = TextAlign.Start,
                fontSize = 42.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                style = TextStyle(lineHeight = 20.sp),
                color = Color.White
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.intro_tagline_2),
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(46.dp))

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                onClick = {
                    navigator.navigate(
                        SetupKeyScreenDestination()
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 22.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
        BackHandler {
            (context as ComponentActivity).finish()
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun IntroScreenPreview() {
//    IntroScreen()
//}