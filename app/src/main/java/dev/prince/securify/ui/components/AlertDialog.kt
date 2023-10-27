package dev.prince.securify.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.Gray
import dev.prince.securify.ui.theme.poppinsFamily

@Composable
fun AlertDialogContent(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmTitle: String
) {
    AlertDialog(
        title = {
            Text(
                text = dialogTitle,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        text = {
            Text(
                text = dialogText,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
                )
            )
        },
        containerColor = Color.White,
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmation()
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        ),
                    text = confirmTitle,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        },
        dismissButton = {
            Text(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .clickable {
                        onDismissRequest()
                    },
                text = "Cancel",
                color = Gray,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    )
}