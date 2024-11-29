package com.example.cred

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cred.component.BankDetails
import com.example.cred.component.CustomCardRow

import com.example.cred.component.LoadingAnimation
import com.example.cred.model.ApiResponse
import com.example.cred.ui.theme.CredTheme
import com.example.cred.viewmodel.MyViewModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CredTheme {
                val viewModel: MyViewModel  = viewModel()
                MyApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(viewModel: MyViewModel) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState2 = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope2 = rememberCoroutineScope()
    var showBottomSheet2 by remember { mutableStateOf(false) }
    val apiData by viewModel.apiData.collectAsState()
    val maxRange = apiData?.items?.firstOrNull()?.open_state?.body?.card?.max_range ?: 487891
    val progress = remember { mutableStateOf(0f) }
    var dragProgress by remember { mutableStateOf(progress) }
    var emi = remember { mutableStateOf("") }
    var duration = remember { mutableStateOf("") }


    // Fetch data (call only once if needed)
    LaunchedEffect(Unit) {
        viewModel.fetchData()
    }


    Scaffold(
        containerColor = Color.Black // Set scaffold background color
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(contentPadding)
        ) {
//            Column {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.Black)
//                        .padding(16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    // Cross Icon
//                    Icon(
//                        imageVector = Icons.Default.Close,
//                        contentDescription = "Close",
//                        tint = Color.White,
//                        modifier = Modifier.clickable {
//                            // Handle close icon click
//                        }
//                    )
//
//                    // Help Icon
//                    Icon(
//                        imageVector = Icons.Default.Build,
//                        contentDescription = "Help",
//                        tint = Color.White,
//                        modifier = Modifier.clickable {
//                            // Handle help icon click
//                        }
//                    )
//                }
//            }
            if (apiData==null) {
                // Show Circular Loader
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                   LoadingAnimation()
                }
            }else {
                if (showBottomSheet) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)// Semi-transparent black overlay
                    )
                    Column{


//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .background(Color.Black)
//                                .padding(16.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            // Cross Icon
//                            Icon(
//                                imageVector = Icons.Default.Close,
//                                contentDescription = "Close",
//                                tint = Color.White,
//                                modifier = Modifier.clickable {
//                                    // Handle close icon click
//                                }
//                            )
//
//                            // Help Icon
//                            Icon(
//                                imageVector = Icons.Default.Build,
//                                contentDescription = "Help",
//                                tint = Color.White,
//                                modifier = Modifier.clickable {
//                                    // Handle help icon click
//                                }
//                            )
//                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 40.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(

                            ) {
                                Text(text = "credit amount", color = Color.Gray)
                                Text(text = "₹${(dragProgress.value * maxRange).toInt()}", color = Color.Gray, fontSize = 20.sp)
                            }
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.clickable {
                                    // Handle close icon click
                                }
                            )
                        }
                    }
                }
                if (!showBottomSheet) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 60.dp) // Add space for the button at the bottom
                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .background(Color.Black)
//                                .padding(16.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            // Cross Icon
//                            Icon(
//                                imageVector = Icons.Default.Close,
//                                contentDescription = "Close",
//                                tint = Color.White,
//                                modifier = Modifier.clickable {
//                                    // Handle close icon click
//                                }
//                            )
//
//                            // Help Icon
//                            Icon(
//                                imageVector = Icons.Default.Build,
//                                contentDescription = "Help",
//                                tint = Color.White,
//                                modifier = Modifier.clickable {
//                                    // Handle help icon click
//                                }
//                            )
//                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 56.dp)
                                .background(Color.Black),


                        ) {

                            apiData?.items.let { items ->
                                val creditItem = items?.get(0)
                                creditItem?.open_state?.body?.title?.let {
                                    Text(
                                        text = it,
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(start = 20.dp, top = 20.dp, bottom = 6.dp)
                                            .shadow(4.dp, RoundedCornerShape(4.dp)), // Adding shadow
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold, // Bold font
                                        letterSpacing = 1.5.sp, // Increase letter spacing
                                        style = TextStyle(
                                            fontFamily = FontFamily.SansSerif,
                                            color = Color.White.copy(alpha = 0.9f) // Slightly transparent
                                        )
                                    )

// Styling for subtitle text
                                    creditItem?.open_state?.body?.subtitle?.let {
                                        Text(
                                            text = it,
                                            color = Color.Gray.copy(alpha = 0.7f), // Change color for contrast
                                            modifier = Modifier
                                                .padding(start = 20.dp)
                                                .shadow(2.dp, RoundedCornerShape(4.dp)), // Adding shadow
                                            fontSize = 15.sp,
                                            fontStyle = FontStyle.Italic, // Italic style for subtitle
                                            style = TextStyle(
                                                fontFamily = FontFamily.SansSerif,
                                                letterSpacing = 1.2.sp // Slightly increase letter spacing
                                            )
                                        )
                                    }
                                }


                            }



                            apiData?.let { RoundedCornerCard(it, dragProgress = dragProgress, maxRange = maxRange, progress = progress) }
                        }
                    }
                }

                // Button at the bottom of the screen
                Button(
                    onClick = {
                        showBottomSheet = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B439B)),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    ) // Only round the top corners
                ) {
                    Text("Proceed to EMI selection", color = Color.White, fontSize = 18.sp)
                }

                // Display the bottom sheet if `showBottomSheet` is true
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState,
                        containerColor = Color.Black,
                        // Set background color for the bottom sheet
                    ) {
                        if (showBottomSheet2) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black)// Semi-transparent black overlay
                            ) {
                                Column {
//                                    Row(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .background(Color.Black),
//                                        horizontalArrangement = Arrangement.SpaceBetween
//                                    ) {
//                                        // Cross Icon
//                                        Icon(
//                                            imageVector = Icons.Default.Close,
//                                            contentDescription = "Close",
//                                            tint = Color.White,
//                                            modifier = Modifier.clickable {
//                                                // Handle close icon click
//                                            }
//                                        )
//
//                                        // Help Icon
//                                        Icon(
//                                            imageVector = Icons.Default.Build,
//                                            contentDescription = "Help",
//                                            tint = Color.White,
//                                            modifier = Modifier.clickable {
//                                                // Handle help icon click
//                                            }
//                                        )
//                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(start=20.dp, end = 20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Column {
                                            Text(text = "credit amount", color = Color.Gray)
                                            Text(
                                                text = "₹${(dragProgress.value * maxRange).toInt()}",
                                                color = Color.Gray,
                                                fontSize = 20.sp
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Close",
                                            tint = Color.White,
                                            modifier = Modifier.clickable {
                                                // Handle close icon click
                                            }
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = "EMI", color = Color.Gray)
                                            Text(
                                                text = "${emi.value}",
                                                color = Color.Gray,
                                                fontSize = 20.sp
                                            )
                                        }
                                        Column {
                                            Text(text = "duration", color = Color.Gray)
                                            Text(
                                                text = "${duration.value}",
                                                color = Color.Gray,
                                                fontSize = 20.sp
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Close",
                                            tint = Color.White,
                                            modifier = Modifier.clickable {
                                                // Handle close icon click
                                            }
                                        )

                                    }

                                }
                            }

                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black)

                        ) {
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = "How do you wish to repay?",
                                color = Color.White,
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                            )
                            Text(
                                text = "choose one of our recommended plans or make your own",
                                color = Color.Gray,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            CustomCardRow(apiData!!, emi, duration)
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = {},
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                            ) {
                                Text(text = "Create your own plan")
                            }
                            Spacer(modifier = Modifier.height(200.dp))

                            Button(
                                onClick = {
                                    showBottomSheet2 = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF3B439B
                                    )
                                ),
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp
                                ) // Only round the top corners
                            ) {
                                Text(
                                    "Select your bank account",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )

                            }
                            if (showBottomSheet2) {
                                ModalBottomSheet(
                                    onDismissRequest = {
                                        showBottomSheet2 = false
                                    },
                                    sheetState = sheetState2,
                                    containerColor = Color.Black // Set background color for the bottom sheet
                                ) {
                                    BankDetails(apiData!!)
//

                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoundedCornerCard(apiData: ApiResponse, dragProgress: MutableState<Float>, maxRange: Int, progress: MutableState<Float>) {

    Card(
        modifier = Modifier
            .fillMaxWidth().height(400.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp), // Rounded corners
        elevation = CardDefaults.cardElevation(8.dp) // Shadow for the card
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            AdjustableCircularSlider(
                progress = progress,
                onProgressChange = { newProgress -> progress.value = newProgress  },
                apiData = apiData,
                dragProgress = dragProgress,
                maxRange = maxRange
            )
            Spacer(modifier = Modifier.height(40.dp))
            

            Text(
                text = "stash is instant money will be credited within seconds.",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AdjustableCircularSlider(
    progress: MutableState<Float>,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    strokeWidth: Dp = 15.dp,
    color: Color = Color(0xFFD7896D),
    backgroundColor: Color = Color(0xFFFFEAE1),
    apiData: ApiResponse,
    dragProgress: MutableState<Float>,
    maxRange: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        val radius = with(LocalDensity.current) { size.toPx() / 2 }

        Canvas(
            modifier = Modifier
                .size(size)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val x = change.position.x - radius
                        val y = radius - change.position.y
                        val theta = Math.toDegrees(atan2(y.toDouble(), x.toDouble())).toFloat()
                        val adjustedTheta = (theta + 360) % 360
                        val clockwiseTheta = (360 - adjustedTheta + 90) % 360

                        // Map angle to progress (0.0 - 1.0)
                        dragProgress.value = clockwiseTheta / 360f
                        onProgressChange(dragProgress.value)
                    }
                }
        ) {
            drawCircle(
                color = backgroundColor,
                style = Stroke(width = strokeWidth.toPx())
            )

            val sweepAngle = 360 * dragProgress.value
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            val knobX = radius + radius * cos(Math.toRadians(sweepAngle - 90.0)).toFloat()
            val knobY = radius + radius * sin(Math.toRadians(sweepAngle - 90.0)).toFloat()
            drawCircle(
                color = Color(0xFFFBC3B1),
                radius = strokeWidth.toPx() * 1.0f,
                center = Offset(knobX, knobY)
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "credit amount", fontSize = 12.sp)
            Text(
                text = "₹${(dragProgress.value * maxRange).toInt()}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Text(text = "@1.04% monthly", fontSize = 10.sp, color = Color(0xFF37AB37))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CredTheme {

    }
}