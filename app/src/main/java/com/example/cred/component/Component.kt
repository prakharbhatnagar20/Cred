package com.example.cred.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.cred.model.ApiResponse
import com.example.cred.model.RepaymentItem
import kotlinx.coroutines.delay


@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        circleValues.forEach { value ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .graphicsLayer {
                        translationY = -value * distance
                    }
                    .background(
                        color = circleColor,
                        shape = CircleShape
                    )
            )
        }
    }

}

@Composable
fun CustomCardRow(apiData: ApiResponse, emi: MutableState<String>, duration: MutableState<String>) {
    val cardColors = listOf(
        Color(0xFFE57373), // Red
        Color(0xFF81C784), // Green
        Color(0xFF64B5F6), // Blue
        Color(0xFFFFD54F), // Yellow
        Color(0xFFBA68C8)  // Purple
    )

    // Extract EMI plans
    val emiPlans = apiData.items[1].open_state.body.items
    var selectedIndex by remember { mutableStateOf(-1) } // Tracks the selected card index

    Column {
        // LazyRow to display cards with padding to prevent clipping on scale
        LazyRow(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp), // Add extra padding on each side
            horizontalArrangement = Arrangement.spacedBy(24.dp) // Increase space between cards
        ) {
            if (emiPlans != null) {
                items(emiPlans.size) { index ->
                    val color = cardColors[index % cardColors.size]
                    val emiPlan = emiPlans[index]
                    val isSelected = selectedIndex == index

                    Card(
                        modifier = Modifier
                            .height(180.dp)
                            .width(170.dp)
                            .padding(8.dp) // Add padding to the card itself to give space for scaling
                            .clickable {
                                selectedIndex = if (selectedIndex == index) -1 else index
                                emi.value = if (selectedIndex == index) emiPlan.emi else ""
                                duration.value = if (selectedIndex == index) emiPlan.duration else ""
                            }
                            .scale(if (isSelected) 1.1f else 1f),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = color
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.TopStart)
                            ) {
                                Spacer(modifier = Modifier.height(10.dp))

                                // Display EMI details
                                Text(
                                    text = emiPlan.title, // Example: "₹4,247 /mo for 12 months"
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Display subtitle
                                Text(
                                    text = emiPlan.subtitle, // Example: "See calculations"
                                    color = Color.White,
                                    fontSize = 15.sp
                                )

                                // Display "recommended" tag if available
                                emiPlan.tag?.let {
                                    Text(
                                        text = it,
                                        color = Color.Yellow,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Conditionally display the selected card details
    }
}

@Composable
fun BankDetails(apiData: ApiResponse) {
    val bankItems = apiData.items[2].open_state.body.items
    var selectedBankItem by remember { mutableStateOf<RepaymentItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Text(
            text = "Where should we send the money?",
            color = Color.White,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        )
        Text(
            text = "Amount will be credited to this account, EMI will also be debited from this account",
            color = Color.Gray,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Display each bank account option
        bankItems?.forEach { bank ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Card(
                    modifier = Modifier
                        .height(50.dp)
                        .width(70.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    // Optionally, place bank icon here
                }
                Column {
                    Text(
                        text = bank.title, // Bank name (e.g., "HDFC BANK")
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Text(
                        text = bank.subtitle.toString(), // Account number or identifier
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(
                    selected = selectedBankItem == bank,
                    onClick = { selectedBankItem = bank },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.White,
                        unselectedColor = Color.Gray
                    )
                )



            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { /* Action to change account */ },
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Text(text = "Change Account")
        }

        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = { /* Action for 1-click KYC */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B439B)
            ),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Text("Tap for 1-click KYC", color = Color.White, fontSize = 18.sp)
        }
    }
}




