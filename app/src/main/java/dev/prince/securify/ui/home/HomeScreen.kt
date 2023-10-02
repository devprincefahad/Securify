package dev.prince.securify.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.ui.destinations.IntroScreenDestination
import dev.prince.securify.ui.destinations.UnlockScreenDestination

@Destination()
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

//    if (!viewModel.isUserLoggedIn) {
////        navigator.navigate(UnlockScreenDestination)
////    } else {
//        navigator.navigate(IntroScreenDestination)
//    }
    Column {
        Text(
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            ),
            text = "Home screen",
            fontSize = 26.sp
        )
    }
//    Toast.makeText(context, "On home screen", Toast.LENGTH_LONG).show()
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen()
//}