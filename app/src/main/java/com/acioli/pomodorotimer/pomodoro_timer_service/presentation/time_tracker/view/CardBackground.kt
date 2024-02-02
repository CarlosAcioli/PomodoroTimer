package com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acioli.pomodorotimer.R
import com.acioli.pomodorotimer.ui.theme.fontFamilyLexend

@Composable
fun CardBackground(
    modifier: Modifier = Modifier,
    focusDuration: String = "",
    shortDuration: String = "",
    cycles: String = "",
    longDuration: String = ""
) {

    val isTextsEmpty = focusDuration.isEmpty() || shortDuration.isEmpty() || cycles.isEmpty() || longDuration.isEmpty()

    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = if(isTextsEmpty) {
            Modifier.size(180.dp)
        } else {
            modifier
        }
    ) {
        Column(
            modifier = modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            if(isTextsEmpty) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                ){

                    Image(
                        painter = painterResource(id = R.drawable.board),
                        contentDescription = "No info",
                        alpha = 0.4f,
                        modifier = Modifier.size(95.dp)
                    )

                    Spacer(modifier = Modifier.height(13.dp))

                    Text(
                        text = "Nothing yet :(",
                        fontFamily = fontFamilyLexend,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        modifier = Modifier.alpha(0.4f)
                    )

                }


            } else {

                Row {
                    Text(
                        text = "Focus time: $focusDuration min",
                        fontFamily = fontFamilyLexend,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                    )
                }

                Row {
                    Text(
                        text = "Short pause: $shortDuration min",
                        fontFamily = fontFamilyLexend,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                    )
                }

                Row {
                    Text(
                        text = "Cycles: $cycles",
                        fontFamily = fontFamilyLexend,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                    )
                }

                Row {
                    Text(
                        text = "Long pause: $longDuration min",
                        fontFamily = fontFamilyLexend,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                    )
                }

            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewTwo() {
    CardBackground()
}