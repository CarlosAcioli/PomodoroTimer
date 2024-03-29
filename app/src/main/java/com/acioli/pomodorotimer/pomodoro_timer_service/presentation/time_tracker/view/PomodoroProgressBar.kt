package com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acioli.pomodorotimer.ui.theme.fontFamilyLexend
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun CustomComponent(
    modifier: Modifier = Modifier,
    canvasSize: Dp = 300.dp,
    indicatorValue: Long = 0L,
    maxIndicatorValue: Long = 25L,
    currentCycle: String = "0",
    maxCycle: String = "0",
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 50f,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.tertiary,
    foregroundIndicatorStrokeWidth: Float = 50f,
//    indicatorStrokeCap: StrokeCap = StrokeCap.Round,
    bigTextFontSize: TextUnit = MaterialTheme.typography.headlineLarge.fontSize,
    bigTextColor: Color = MaterialTheme.colorScheme.onSurface,
    bigTextSuffix: String = "",
    smallText: String = "Focus time",
    smallTextFontSize: TextUnit = MaterialTheme.typography.bodySmall.fontSize,
    smallTextColor: Color = MaterialTheme.colorScheme.onBackground
) {
    var allowedIndicatorValue by remember {
        mutableStateOf(maxIndicatorValue)
    }

    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue) {
        indicatorValue
    } else {
        maxIndicatorValue
    }

    var animatedIndicatorValue by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toInt().toFloat()
    }

    val percentage =
        (animatedIndicatorValue / maxIndicatorValue.toInt()) * 100

    val sweepAngle by animateIntAsState(
        targetValue = (2.4 * percentage).toInt(),
        animationSpec = tween(1000), label = ""
    )

    val totalTimeInSeconds = maxIndicatorValue
    var timeLeft = remember { mutableLongStateOf(totalTimeInSeconds.toLong()) }

    val timerText = remember { mutableStateOf(timeLeft.longValue.timeFormat()) }

    val receivedValue by animateIntAsState(
        targetValue = allowedIndicatorValue.toInt(),
        animationSpec = tween(1000), label = ""
    )

    val animatedBigTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0L)
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        else
            bigTextColor,
        animationSpec = tween(1000), label = ""
    )

    val animatedCurrentCycleColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0L)
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        else
            bigTextColor,
        animationSpec = tween(1000), label = ""
    )

    val animateSmallTextColor by animateColorAsState(
        targetValue = if(allowedIndicatorValue == 0L)
            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
        else
            smallTextColor,
        animationSpec = tween(1000), label = ""
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {
                val componentSize = size / 1.25f
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
//                    indicatorStokeCap = indicatorStrokeCap
                )
                foregroundIndicator(
                    sweepAngle = sweepAngle.toFloat(),
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
//                    indicatorStokeCap = indicatorStrokeCap
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElements(
            bigText = receivedValue.toLong().timeFormat(),
            bigTextFontSize = bigTextFontSize,
            bigTextColor = animatedBigTextColor,
            smallText = smallText,
            smallTextColor = animateSmallTextColor,
            currentCycle = currentCycle,
            maxCycle = maxCycle,
            cycleColor = animatedCurrentCycleColor
        )
    }

}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
//    indicatorStokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

@Composable
fun EmbeddedElements(
    bigText: String,
    bigTextFontSize: TextUnit,
    bigTextColor: Color,
    smallText: String,
    smallTextColor: Color,
    currentCycle: String,
    maxCycle: String,
    cycleColor: Color
) {
    Text(
        text = smallText,
        color = smallTextColor,
        fontSize = 30.sp,
        textAlign = TextAlign.Center,
        fontFamily = fontFamilyLexend
    )
    Text(
        text = bigText,
        color = bigTextColor,
        fontSize = bigTextFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "$currentCycle/$maxCycle",
        color = cycleColor,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}

private const val FORMAT = "%02d:%02d"

fun Long.timeFormat(): String = String.format(
    FORMAT,
    TimeUnit.SECONDS.toMinutes(this),
    TimeUnit.SECONDS.toSeconds(this) % 60
)

@Composable
@Preview(showBackground = true)
fun CustomComponentPreview() {
    CustomComponent()
}