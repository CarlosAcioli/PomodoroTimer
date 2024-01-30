package com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acioli.pomodorotimer.ui.theme.fontFamilyLexend

@Composable
fun CardBackground(
    modifier: Modifier = Modifier,
    focusDuration: String = "empty duration",
    shortDuration: String = "empty duration",
    cycles: String = "empty duration",
    longDuration: String = "empty duration"
) {

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD1D1D1)
        )
    ) {
        Column(
            modifier = Modifier
                .height(300.dp)
                .width(250.dp)
                .padding(5.dp),
            verticalArrangement = Arrangement.SpaceAround,
        ) {

            Row {
                Text(
                    text = "Focus time: $focusDuration min",
                    fontFamily = fontFamilyLexend,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            }

            Row {
                Text(
                    text = "Short pause: $shortDuration min",
                    fontFamily = fontFamilyLexend,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            }

            Row {
                Text(
                    text = "Cycles: $cycles",
                    fontFamily = fontFamilyLexend,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            }

            Row {
                Text(
                    text = "Long pause: $longDuration min",
                    fontFamily = fontFamilyLexend,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewTwo() {
    CardBackground()
}