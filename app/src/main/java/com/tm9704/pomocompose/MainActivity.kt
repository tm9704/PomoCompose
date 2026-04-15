package com.tm9704.pomocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.tm9704.pomocompose.ui.theme.PomoComposeTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PomoComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = colorResource(R.color.pomo_bg_dark)
                ) { innerPadding ->
                    PomoScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PomoScreen(modifier: Modifier = Modifier) {
    var isRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(1500) }
    var isState by remember { mutableStateOf(true) }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    LaunchedEffect(key1 = isRunning) {
        if(isRunning) {
            while(timeLeft > 0){
                delay(1000L)
                timeLeft -= 1
            }

            if(timeLeft == 0) {
                isState = !isState
                timeLeft = if (isState) 1500 else 300
                isRunning = false // or true? auto start?
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(R.dimen.timer_margin_top),
                start = dimensionResource(R.dimen.spacing_screen_edge),
                end = dimensionResource(R.dimen.spacing_screen_edge)
            )
    ) {
        PomoLabel(isState)
        Spacer(modifier = Modifier
            .height(dimensionResource(R.dimen.spacing_element_medium))
        )
        PomoTimer(formattedTime)
        Spacer(modifier = Modifier
            .height(dimensionResource(R.dimen.spacing_timer_to_btn))
        )
        PomoButtons(
            isRunning = isRunning,
            onToggleClick = { isRunning = !isRunning },
            onResetClick = {
                isRunning = false
                timeLeft = if (isState) 1500 else 300
            }
        )
    }
}

@Composable
fun PomoLabel (isState: Boolean){
    Text(
        text = if (isState) stringResource(R.string.label_state_focus) else stringResource(R.string.label_state_break),
        color = if (isState) colorResource(R.color.pomo_focus_red) else colorResource(R.color.pomo_break_green),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun PomoTimer(formattedTime: String,modifier: Modifier = Modifier) {
    Text(
        text = formattedTime,
        fontSize = 80.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.pomo_text_white),
        modifier = modifier
    )
}

@Composable
fun PomoButtons(
    isRunning: Boolean,
    onToggleClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_button_gap)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Button(
            onClick = onToggleClick,
            modifier = Modifier.size(
                width = dimensionResource(R.dimen.button_width),
                height = dimensionResource(R.dimen.button_height)
            ),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_button)),
        ) {
           Text(
               text = if (isRunning) stringResource(R.string.btn_pause) else stringResource(R.string.btn_start),
               fontSize = 16.sp,
               fontWeight = FontWeight.Bold
           )
        }

        OutlinedButton(
            onClick = onResetClick,
            modifier = Modifier.size(
                width = dimensionResource(R.dimen.button_width),
                height = dimensionResource(R.dimen.button_height)
            ),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_button)),
        ) {
            Text(
                text = stringResource(R.string.btn_reset),
                fontSize = 16.sp,
                color = colorResource(R.color.pomo_text_white)
            )
        }
    }
}