package com.differentshadow.personalfinance.ui.components

import android.util.Log
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.differentshadow.personalfinance.domain.model.DonutChartData
import com.differentshadow.personalfinance.domain.model.DonutChartDataCollection
import com.differentshadow.personalfinance.domain.model.DrawingAngles
import com.differentshadow.personalfinance.domain.model.STROKE_SIZE_SELECTED
import com.differentshadow.personalfinance.domain.model.STROKE_SIZE_UNSELECTED
import com.differentshadow.personalfinance.domain.model.calculateGapAngle
import com.differentshadow.personalfinance.domain.model.findSweepAngle
import com.differentshadow.personalfinance.domain.model.handleCanvasTap
import com.differentshadow.personalfinance.ui.theme.PoppinsFamily


//val test = listOf(
//DonutChartData(amount = 90f, color = Color(0xFF8BC34A), title = "Grocery"),
//DonutChartData(amount = 323f, color = Color(0xFF2196F3), title = "Dining"),
//DonutChartData(amount = 323f, color = Color(0xFF9C27B0), title = "Transportation"),
//DonutChartData(amount = 323f, color = Color(0xFF009688), title = "Utility"),
//DonutChartData(amount = 323f, color = Color(0xFF795548), title = "Rent/Mortgage"),
//DonutChartData(amount = 323f, color = Color(0xFFE91E63), title = "Entertainment"),
//DonutChartData(amount = 323f, color = Color(0xFFCDDC39), title = "Health"),
//DonutChartData(amount = 323f, color = Color(0xFFFF9800), title = "Fitness"),
//DonutChartData(amount = 323f, color = Color(0xFF3F51B5), title = "Clothing"),
//DonutChartData(amount = 323f, color = Color(0xFFFFC107), title = "Personal Care"),
//DonutChartData(amount = 323f, color = Color(0xFF00BCD4), title = "Education"),
//DonutChartData(amount = 323f, color = Color(0xFFFF5722), title = "Travel"),
//DonutChartData(amount = 323f, color = Color(0xFFF43F5E), title = "Gift and Donation"),
//DonutChartData(amount = 323f, color = Color(0xFF673AB7), title = "Home Maintenance"),
//DonutChartData(amount = 323f, color = Color(0xFF607D8B), title = "Insurance"),
//DonutChartData(amount = 323f, color = Color(0xFF4CAF50), title = "Savings and Investment"),
//DonutChartData(amount = 323f, color = Color(0xFF9E9E9E), title = "Miscellaneous")
//))


private class DonutChartState(
    val state: State = State.Unselected,
    val inactiveStrokeWidth: Dp = STROKE_SIZE_UNSELECTED,
    val activeStrokeWidth: Dp = STROKE_SIZE_SELECTED,
) {
    val stroke: Dp
        get() = when (state) {
            State.Selected -> activeStrokeWidth
            State.Unselected -> inactiveStrokeWidth
        }

    enum class State {
        Selected, Unselected
    }
}

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    chartSize: Dp = 350.dp,
    index: Int,
    onSelectedChange: (Int) -> Unit = {},
    data: DonutChartDataCollection,
    gapPercentage: Float = 0.05f,
    inactiveStrokeWidth: Dp = STROKE_SIZE_UNSELECTED,
    activeStrokeWidth: Dp = STROKE_SIZE_SELECTED,
    selectionView: @Composable (selectedItem: DonutChartData?) -> Unit = {},
) {
    val selectedIndex = remember { mutableStateOf(index) }
    val animationTargetState = (0..data.items.size).map {
        remember {
            mutableStateOf(
                DonutChartState(
                    inactiveStrokeWidth = inactiveStrokeWidth,
                    activeStrokeWidth = activeStrokeWidth
                )
            )
        }
    }
    val animValues = (0..data.items.size).map {
        animateDpAsState(
            targetValue = animationTargetState[it].value.stroke,
            animationSpec = TweenSpec(700), label = ""
        )
    }
    val anglesList: MutableList<DrawingAngles> = remember { mutableListOf() }
    val gapAngle = data.calculateGapAngle(gapPercentage)
    var center = Offset(0f, 0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(chartSize)
    ) {
        Box(
            modifier = modifier
                .weight(1f, true)
                .height(chartSize)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier
                    .size(chartSize)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { tapOffset ->
                                handleCanvasTap(
                                    center = center,
                                    tapOffset = tapOffset,
                                    anglesList = anglesList,
                                    currentSelectedIndex = selectedIndex.value,
                                    currentStrokeValues = animationTargetState.map { it.value.stroke.toPx() },
                                    onItemSelected = { index ->
                                        selectedIndex.value = index
                                        onSelectedChange(index)
                                        animationTargetState[index].value = DonutChartState(
                                            DonutChartState.State.Selected,
                                            inactiveStrokeWidth = inactiveStrokeWidth,
                                            activeStrokeWidth = activeStrokeWidth
                                        )
                                    },
                                    onItemDeselected = { index ->
                                        animationTargetState[index].value = DonutChartState(
                                            DonutChartState.State.Unselected,
                                            inactiveStrokeWidth = inactiveStrokeWidth,
                                            activeStrokeWidth = activeStrokeWidth
                                        )
                                    },
                                    onNoItemSelected = {
                                        selectedIndex.value = index
                                        onSelectedChange(-1)
                                    }
                                )
                            }
                        )
                    },
                onDraw = {
                    val defaultStrokeWidth = inactiveStrokeWidth.toPx()
                    center = this.center
                    anglesList.clear()
                    var lastAngle = 0f
                    data.items.forEachIndexed { ind, item ->
                        val sweepAngle = data.findSweepAngle(ind, gapPercentage)
                        anglesList.add(DrawingAngles(lastAngle, sweepAngle))
                        val strokeWidth = animValues[ind].value.toPx()
                        drawArc(
                            color = item.color,
                            startAngle = lastAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = Offset(defaultStrokeWidth / 2, defaultStrokeWidth / 2),
                            style = Stroke(strokeWidth, cap = StrokeCap.Round),
                            size = Size(
                                size.width - defaultStrokeWidth,
                                size.height - defaultStrokeWidth
                            )
                        )
                        lastAngle += sweepAngle + gapAngle
                    }
                }
            )
            selectionView(if (selectedIndex.value >= 0) data.items[selectedIndex.value] else null)
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1.0f, true)) {
            itemsIndexed(data.items) { index, item ->
//                Button(
//                    modifier = Modifier.padding(horizontal = 6.dp),
//                    onClick = {
//                        val prevIndex = selectedIndex.value
//                        selectedIndex.value = index
//
//                        if (prevIndex != -1) {
//                            animationTargetState[prevIndex].value = DonutChartState(
//                                DonutChartState.State.Unselected,
//                                inactiveStrokeWidth = inactiveStrokeWidth,
//                                activeStrokeWidth = activeStrokeWidth
//                            )
//                        }
//
//
//                        if (selectedIndex.value == prevIndex) {
//                            selectedIndex.value = -1
//                            animationTargetState[index].value = DonutChartState(
//                                DonutChartState.State.Unselected,
//                                inactiveStrokeWidth = inactiveStrokeWidth,
//                                activeStrokeWidth = activeStrokeWidth
//                            )
//                        } else {
//                            animationTargetState[index].value = DonutChartState(
//                                DonutChartState.State.Selected,
//                                inactiveStrokeWidth = inactiveStrokeWidth,
//                                activeStrokeWidth = activeStrokeWidth
//                            )
//                        }
//                    },
//                    colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.surface),
//                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .height(12.dp)
//                            .width(12.dp)
//                            .absoluteOffset(x = (-8).dp)
//                            .background(
//                                color = item.color,
//                                shape = RoundedCornerShape(6.dp)
//                            )
//
//                    )
//                    Text(text = item.category, style = MaterialTheme.typography.labelLarge)
//                }
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(12.dp)
                            .width(12.dp)
                            .absoluteOffset(x = (-8).dp)
                            .background(
                                color = item.color,
                                shape = RoundedCornerShape(6.dp)
                            )

                    )
                    Text(text = item.category, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}