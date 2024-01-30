package com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.view

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acioli.pomodorotimer.R
import com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.PomodoroViewModel
import com.acioli.pomodorotimer.ui.theme.fontFamilyLexend
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {

    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
//    val mpStart: MediaPlayer = MediaPlayer.create(context, R.raw.start_timer)
//    val mpPause: MediaPlayer = MediaPlayer.create(context, R.raw.pause_timer)
//    val mpStop: MediaPlayer = MediaPlayer.create(context, R.raw.end_timer)

    var focusDuration by remember { mutableStateOf("") }
    var shortPauseDuration by remember { mutableStateOf("") }
    var cycleUntilLongPause by remember { mutableStateOf("") }
    var longPauseDuration by remember { mutableStateOf("") }

    val mainTrackerViewModel = viewModel<PomodoroViewModel>()
    val mainTimerState = mainTrackerViewModel.state.collectAsState()
    val shortPauseState = mainTrackerViewModel.pause.collectAsState()
    val longPauseSate = mainTrackerViewModel.longPause.collectAsState()
    val cycleState = mainTrackerViewModel.cycleState.collectAsState()

    Scaffold(
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(2f)
                            .padding(top = 30.dp)
                    ) {

                        CustomComponent(
                            indicatorValue = if (shortPauseState.value > 0.seconds) shortPauseState.value.inWholeSeconds else if (longPauseSate.value > 0.seconds) longPauseSate.value.inWholeSeconds else mainTimerState.value.inWholeSeconds,
                            maxIndicatorValue = if (shortPauseState.value > 0.seconds && shortPauseDuration.isNotBlank()) shortPauseDuration.toInt().minutes.inWholeSeconds else if (longPauseSate.value > 0.seconds && longPauseDuration.isNotBlank()) longPauseDuration.toInt().minutes.inWholeSeconds else if (mainTimerState.value > 0.seconds && focusDuration.isNotBlank()) focusDuration.toInt().minutes.inWholeSeconds else 10.minutes.inWholeSeconds,
                            smallText = if (shortPauseState.value > 0.seconds) "Pause time" else if (longPauseSate.value > 0.seconds) "Long pause" else "Focus time"
                        )

                        Log.d(
                            "TAG",
                            "MainScreen: ${mainTimerState.value.absoluteValue.inWholeSeconds}"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "What you've choose",
                            fontFamily = fontFamilyLexend,
                            fontWeight = FontWeight.Normal,
                            fontSize = 25.sp
                        )

                        CardBackground(
                            modifier = Modifier.fillMaxWidth(),
                            focusDuration,
                            shortPauseDuration,
                            cycleUntilLongPause,
                            longPauseDuration
                        )

                        Button(
                            onClick = {
                                mainTrackerViewModel.start(
                                    focusDuration.toInt().minutes,
                                    shortPauseDuration.toInt().minutes,
                                    longPauseDuration.toInt().minutes,
                                    cycleUntilLongPause.toInt()
                                )

                            }
                        ) {
                            Text(text = "Click")
                        }

                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Editar",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable {
                            isSheetOpen = true
                        }
                        .size(32.dp)
                        .padding(end = 10.dp, top = 10.dp),
                )

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configurações",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 10.dp, top = 10.dp)
                        .clickable {

                        }
                )

                if (isSheetOpen) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            isSheetOpen = false
                        },
                        sheetState = sheetState
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.width(280.dp)
                            ) {

                                Text(
                                    text = "Focus time duration:",
                                    fontFamily = fontFamilyLexend,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray.copy(alpha = 0.8f)
                                )

                                Text(
                                    text = focusDuration.ifBlank { "0" },
                                    fontSize = 20.sp,
                                    fontFamily = fontFamilyLexend,
                                    fontWeight = FontWeight.Normal
                                )

                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = focusDuration,
                                onValueChange = { focusDuration = it },
                                singleLine = true,
                                keyboardActions = KeyboardActions(onDone = { focusRequester1.requestFocus() })
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.width(280.dp)
                            ) {

                                Text(
                                    text = "Short pause duration:",
                                    fontFamily = fontFamilyLexend,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray.copy(alpha = 0.8f)
                                )

                                Text(
                                    text = shortPauseDuration.ifBlank { "0" },
                                    fontSize = 20.sp,
                                    fontFamily = fontFamilyLexend,
                                    fontWeight = FontWeight.Normal
                                )

                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = shortPauseDuration,
                                onValueChange = {
                                    shortPauseDuration = it
                                },
                                singleLine = true,
                                modifier = Modifier.focusRequester(focusRequester1),
                                keyboardActions = KeyboardActions(onDone = { focusRequester2.requestFocus() })
                            )

                            Spacer(modifier = Modifier.height(10.dp))


                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.width(280.dp)
                            ) {

                                Text(
                                    text = "Cycles until long pause:",
                                    fontFamily = fontFamilyLexend,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray.copy(alpha = 0.8f)
                                )

                                Text(
                                    text = cycleUntilLongPause.ifBlank { "0" },
                                    fontSize = 20.sp,
                                    fontFamily = fontFamilyLexend,
                                    fontWeight = FontWeight.Normal
                                )

                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = cycleUntilLongPause,
                                onValueChange = {
                                    cycleUntilLongPause = it
                                },
                                singleLine = true,
                                modifier = Modifier.focusRequester(focusRequester2),
                                keyboardActions = KeyboardActions(onDone = { focusRequester3.requestFocus() })
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.width(280.dp)
                            ) {

                                Text(
                                    text = "Long pause duration:",
                                    fontFamily = fontFamilyLexend,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray.copy(alpha = 0.8f)
                                )

                                Text(
                                    text = longPauseDuration.ifBlank { "0" },
                                    fontSize = 20.sp,
                                    fontFamily = fontFamilyLexend,
                                    fontWeight = FontWeight.Normal
                                )

                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = longPauseDuration,
                                onValueChange = {
                                    longPauseDuration = it
                                },
                                singleLine = true,
                                modifier = Modifier.focusRequester(focusRequester3),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            )

                        }

                    }
                }

            }

        }
    }

//    if( focusDuration.isNotBlank() && mainTimerState.value.absoluteValue == focusDuration.toInt().minutes){
//        mpStart.start()
//    } else if(shortPauseDuration.isNotBlank() && shortPauseState.value.absoluteValue == shortPauseDuration.toInt().minutes  ) {
//        mpStop.start()
//    } else if(longPauseSate.value == 1.seconds) {
//        mpPause.start()
//    } else if(longPauseDuration.isNotBlank() && longPauseSate.value.absoluteValue == longPauseDuration.toInt().minutes) {
//        mpStop.start()
//    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MainScreen()
}