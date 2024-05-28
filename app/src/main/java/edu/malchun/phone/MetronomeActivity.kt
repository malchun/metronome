package edu.malchun.phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.malchun.phone.ui.theme.ColorFamily
import edu.malchun.phone.ui.theme.MetronomeTheme
import kotlinx.coroutines.delay
import kotlin.math.min

class MetronomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetronomeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    BpmLayout()
                }
            }
        }
    }
}

@Composable
fun EditBPSField(value: String,
                 onValueChange: (String) -> Unit,
                 modifier: Modifier = Modifier) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text("Input bpm") }
    )
}


@Composable
fun TemplateText(text: String, bpm: Int, modifier: Modifier = Modifier) {
    Text(
        text = "$text $bpm!",
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

@Composable
fun BlinkingBox(bpm: Int) {
    var isTransparent by remember { mutableStateOf(false) }

    LaunchedEffect(bpm) {
        while (true) {
            isTransparent = !isTransparent
            delay(countDuration(bpm).toLong())
        }
    }

    val color by animateColorAsState(
        targetValue = if (isTransparent) Color.Transparent else Color.Red,
        animationSpec = tween(durationMillis = countDuration(bpm)),
        label = ""
    )

    Box(
        // Full window width square modifier
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
            .background(color)
    )
}

@Preview(showBackground = true)
@Composable
fun BpmLayout() {
    var bpmInput by remember { mutableStateOf("") }
    val bpm = bpmInput.toIntOrNull() ?: 0

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EditBPSField(bpmInput, { bpmInput = it })
        TemplateText("bpm: ", bpm)
        BlinkingBox(bpm)
        TemplateText("duration: ", countDuration(bpm))
    }
}

fun countDuration(bpm: Int): Int {
    if (bpm == 0) return 500 // ones per second
    return 60000 / (bpm * 2) // fade happens twice in a beat
}