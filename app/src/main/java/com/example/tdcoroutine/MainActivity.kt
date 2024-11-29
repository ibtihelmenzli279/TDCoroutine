package com.example.tdcoroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TdcoroutineTheme()
        }
    }
}

@Composable
fun TdcoroutineTheme() {
    var xPosition by remember { mutableStateOf(100f) }
    var yPosition by remember { mutableStateOf(100f) }
    var isMoving by remember { mutableStateOf(false) }
    var direction by remember { mutableStateOf(1) }
    var movementJob by remember { mutableStateOf<Job?>(null) }

    // Start movement coroutine
    fun startMovement() {
        isMoving = true
        movementJob = CoroutineScope(Dispatchers.Main).launch {
            while (isMoving) {
                if (direction == 1) {
                    xPosition += 10f
                    if (xPosition > 500f) {
                        direction = -1
                        yPosition += 50f
                    }
                } else {
                    xPosition -= 10f
                    if (xPosition < 100f) {
                        direction = 1
                        yPosition += 50f
                    }
                }
                delay(100L)
            }
        }
    }

    // Reset movement state
    fun resetMovement() {
        isMoving = false
        movementJob?.cancel()
        xPosition = 100f
        yPosition = 100f
        direction = 1
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0xFF800080), // Violette
                radius = 50f,
                center = Offset(xPosition, yPosition)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { startMovement() },
                enabled = !isMoving // Disable button while moving
            ) {
                Text(text = "Start Movement")
            }

            Button(
                onClick = { resetMovement() }
            ) {
                Text(text = "Reset")
            }
        }
    }
}
