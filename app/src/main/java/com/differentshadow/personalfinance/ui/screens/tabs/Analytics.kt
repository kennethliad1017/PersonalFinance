package com.differentshadow.personalfinance.ui.screens.tabs

import android.graphics.PointF
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntRect
import androidx.compose.ui.unit.toSize
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.model.AnalyticsUIState
import com.differentshadow.personalfinance.domain.model.Expense
import com.differentshadow.personalfinance.ui.components.TransactionItem
import com.differentshadow.personalfinance.ui.components.items
import com.differentshadow.personalfinance.ui.customanimation.layout.animatedNavBarMeasurePolicy
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.ui.theme.PoppinsFamily
import com.differentshadow.personalfinance.utils.TimeFrame
import com.differentshadow.personalfinance.utils.toCurrency
import com.differentshadow.personalfinance.utils.toDateStringList
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Currency
import kotlin.math.roundToInt


val graphDataSample = listOf(
    Expense(1599119580, BigDecimal(10365.763144)),
    Expense(1599119520, BigDecimal(10354.38030756)),
    Expense(1599119460, BigDecimal(10349.18557836)),
    Expense(1599119400, BigDecimal(10346.1234222)),
    Expense(1599119340, BigDecimal(10346.88896124)),
    Expense(1599119280, BigDecimal(10344.2551424)),
    Expense(1599119220, BigDecimal(10348.25599524)),
    Expense(1599119160, BigDecimal(10348.34713084)),
    Expense(1599119100, BigDecimal(10348.28333592)),
    Expense(1599119040, BigDecimal(10347.37197992)),
    Expense(1599118980, BigDecimal(10347.01655108)),
    Expense(1599118920, BigDecimal(10340.39099296)),
    Expense(1599118860, BigDecimal(10340.9742608)),
    Expense(1599118800, BigDecimal(10332.64446696)),
)

fun generatePath(data: List<Expense>, size: Size): Path {
    val path = Path()
    val numberEntries = data.size - 1
    val weekWidth = size.width / numberEntries

    val max = data.maxBy { it.amount }
    val min = data.minBy { it.amount } // will map to x= 0, y = height
    val range = max.amount - min.amount
    val heightPxPerAmount = size.height / range.toFloat()

    data.forEachIndexed { i, balance ->
        if (i == 0) {
            path.moveTo(
                0f,
                size.height - (balance.amount - min.amount).toFloat() *
                        heightPxPerAmount
            )
        }
        val balanceX = i * weekWidth
        val balanceY = size.height - (balance.amount - min.amount).toFloat() *
                heightPxPerAmount
        path.lineTo(balanceX, balanceY)
    }
    return path
}

fun generateSmoothPath(data: List<Expense>, size: Size, paddingHorizontal: Float = 0f, paddingVertical: Float = 0f): Path {
    val path = Path()

    val numberEntries = data.size - 1
    val totalWidth = size.width  - (2 * paddingHorizontal)
    val weekWidth = totalWidth / numberEntries

    val max = data.maxBy { it.amount }
    val min = data.minBy { it.amount } // will map to x= 0, y = height
    val range = max.amount - min.amount
    val heightPxPerAmount = (size.height - (paddingVertical)) / range.toFloat()

    var previousBalanceX = paddingHorizontal
    var previousBalanceY = (size.height - (data[0].amount - min.amount).toFloat() * heightPxPerAmount)
    data.forEachIndexed { i, balance ->
        if (i == 0) {
            path.moveTo(
                paddingHorizontal,
                (size.height - (balance.amount - min.amount).toFloat() *
                        heightPxPerAmount)
            )

        }

        val balanceX = paddingHorizontal + (i * weekWidth)
        val balanceY = (size.height - (balance.amount - min.amount).toFloat() *
                heightPxPerAmount)
        // to do smooth curve graph - we use cubicTo, uncomment section below for non-curve
        val controlPoint1 = PointF(((balanceX + previousBalanceX) / 2f), previousBalanceY)
        val controlPoint2 = PointF(((balanceX + previousBalanceX) / 2f), balanceY)
        path.cubicTo(
            controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y,
            balanceX, balanceY
        )

        previousBalanceX = balanceX
        previousBalanceY = balanceY
    }
    return path
}


fun DrawScope.drawHighlight(
    highlight: Int?,
    graphData: List<Expense>,
    textMeasurer: TextMeasurer,
    labelTextStyle: TextStyle,
    color: Color,
    textColor: Color,
    hintColor: Color,
    paddingHorizontal: Float = 0f,
    paddingVertical: Float = 0f,
    preferCurrency: Currency,
) {
    val selectedValue = highlight ?: 0
    val amount = graphData[selectedValue].amount
    val minAmount = graphData.minBy {  it.amount }.amount
    val range = graphData.maxBy { it.amount }.amount - minAmount
    val percentageHeight = ((amount - minAmount).toFloat() / range.toFloat())
    val pointY = (size.height - ((size.height - paddingVertical) * percentageHeight))
    //draw vertical line on graph
    val x = paddingHorizontal + (selectedValue * ((size.width - (2 * paddingHorizontal)) / (graphData.size - 1)))
    drawLine(
        hintColor,
        start = Offset(x, pointY),
        end = Offset(x, size.height),
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
    )

    // TODO: draw circle's initial position on the current date selected
    // draw hit circle on graph
    drawCircle(
        color = color,
        radius = 4.dp.toPx(),
        center = Offset(x, pointY)
    )

    // draw info box
    val textLayoutResult = textMeasurer.measure(text = amount.setScale(2, RoundingMode.UP).toCurrency(preferCurrency, 2), style = labelTextStyle)
    val highlightContainerSize = (textLayoutResult.size).toIntRect().inflate(4.dp.roundToPx()).size
    val boxTopLeft = (x - (highlightContainerSize.width / 2f))
        .coerceIn(0f, size.width - highlightContainerSize.width)

    drawRoundRect(
        color,
        topLeft = Offset(boxTopLeft, pointY - (paddingVertical * 2f)),
        size = highlightContainerSize.toSize(),
        cornerRadius = CornerRadius(8.dp.toPx())
    )
    drawText(
        textLayoutResult,
        color = textColor,
        topLeft = Offset(boxTopLeft + 4.dp.toPx(), pointY - (paddingVertical * 1.75f))
    )
}

@Composable
fun Tab(
    content: @Composable () -> Unit,
) {

    var itemPositions by remember { mutableStateOf(listOf<Offset>()) }
    val measurePolicy = animatedNavBarMeasurePolicy {
        itemPositions = it.map { xCord ->
            Offset(xCord, 0f)
        }
    }

    Layout(
        modifier = Modifier
            .graphicsLayer {
                clip = true
            }, content = content, measurePolicy = measurePolicy
    )
}

@Composable
fun LineChart(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier)
}

@Composable
fun AnalyticsScreen(
    preferCurrency: Currency,
    startDate: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)),
    endDate: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        .plusDays(6),
    uiState: AnalyticsUIState,
    graphData: List<Expense> = listOf(),
    selectedTimeFrame: TimeFrame,
    onSelectedTime: (value: TimeFrame) -> Unit,
    onNextDateRange: () -> Unit = {},
    onPrevDateRange: () -> Unit = {},
    tabNavigate: (dest: String) -> Unit,
    onNavigateAddTransaction: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val localView = LocalView.current
    val screenWidth = configuration.screenWidthDp - 32
    val tabWidth = (screenWidth / 4f)
    val screenHeightCard = configuration.screenHeightDp * 0.36f
    val labelStyle = MaterialTheme.typography.labelSmall

    val lineChartColor = MaterialTheme.colorScheme.tertiary
    val textColor = MaterialTheme.colorScheme.onSecondary
    val hintColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)

    val startDateString = startDate.format(
        DateTimeFormatter.ofPattern(
            when (selectedTimeFrame) {
                TimeFrame.WEEKLY -> {
                    "d MMM yyyy"
                }

                TimeFrame.MONTHLY -> {
                    "MMM yyyy"
                }

                TimeFrame.ANNUALLY -> {
                    "yyyy"
                }

                TimeFrame.ALL -> {
                    "d MMM yyyy"
                }
            }
        )
    )
    val endDateString = endDate.format(
        DateTimeFormatter.ofPattern(
            when (selectedTimeFrame) {
                TimeFrame.WEEKLY -> {
                    "d MMM yyyy"
                }

                TimeFrame.MONTHLY -> {
                    "MMM yyyy"
                }

                TimeFrame.ANNUALLY -> {
                    "yyyy"
                }

                TimeFrame.ALL -> {
                    "d MMM yyyy"
                }
            }
        )
    )


    val animationProgress = remember {
        Animatable(0f)
    }
    var highlightedGraphData  by remember {
        mutableStateOf<Int?>(null)
    }

    LaunchedEffect(key1 = highlightedGraphData) {
        if (highlightedGraphData != null) {
            localView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }


    LaunchedEffect(key1 = graphData, block = {
        animationProgress.animateTo(1f, tween(3000))
    })
    val textMeasurer = rememberTextMeasurer()
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier
            .safeDrawingPadding(),
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp),
        bottomBar = {
            BottomAppBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = index == 1,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        onClick = { tabNavigate(screen.route) },
                        icon = {
                            Icon(
                                painterResource(id = screen.icon),
                                contentDescription = "Localized description",
                            )
                        })
                }
            }
        }) {
        if (graphData.isEmpty() && uiState.analytics.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.analytics_pana),
                    contentDescription = null
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.your_financial_analytics_will_become_available_as_you_start_recording_expenses),
                    fontFamily = PoppinsFamily,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { onNavigateAddTransaction() }) {
                    Text(text = stringResource(R.string.add_transaction))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

                Spacer(modifier = Modifier.height(16.dp))
               Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeightCard.dp)
                ) {
                   if (graphData.isNotEmpty()) {

                       LineChart(modifier = Modifier
                           .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
                           .aspectRatio(3 / 2f)
                           .fillMaxWidth()
                           .align(alignment = Alignment.CenterHorizontally)
                           .pointerInput(Unit) {
                               detectTapGestures {
                                   coroutineScope.launch {
                                       animationProgress.snapTo(0f)
                                       animationProgress.animateTo(1f, tween(3000))
                                   }
                               }

                           }
                           .pointerInput(Unit) {
                               detectDragGesturesAfterLongPress(
                                   onDragStart = {offset ->
                                       highlightedGraphData =
                                           (offset.x / ((size.width - 16.dp.toPx()) / (graphData.size - 1))).roundToInt()
                                   },
//                                   onDragEnd = { highlightedGraphData = null },
                                   onDragCancel = { highlightedGraphData = null },
                               )
                               { change, _ ->
                                   highlightedGraphData =
                                       (change.position.x / ((size.width - 16.dp.toPx()) / (graphData.size - 1))).roundToInt()
                               }
                           }
                           .drawWithCache {
                               // Adjust the offset to add padding
                               val offsetX = 16.dp.toPx() // Horizontal padding
                               val offsetY = 16.dp.toPx() // Vertical padding

                               val path = generateSmoothPath(graphData, size, offsetX, offsetY)

                               val pathMeasure = PathMeasure()
                               pathMeasure.setPath(path, false)

                               val filledPath = Path()

                               filledPath.addPath(path)
                               filledPath.relativeLineTo(0f, size.height)
                               filledPath.lineTo(0f, size.height)
                               filledPath.close()

                               onDrawBehind {
                                   val barWidthPx = 1.dp.toPx()
                                   drawRect(hintColor, style = Stroke(barWidthPx))

                                   val verticalLines = 4
                                   val verticalSize = size.width / (verticalLines + 1)
                                   repeat(verticalLines) { i ->
                                       val startX = verticalSize * (i + 1)
                                       drawLine(
                                           hintColor,
                                           start = Offset(startX, 0f),
                                           end = Offset(startX, size.height),
                                           strokeWidth = barWidthPx
                                       )
                                   }
                                   val horizontalLines = 3
                                   val sectionSize = size.height / (horizontalLines + 1)
                                   repeat(horizontalLines) { i ->
                                       val startY = sectionSize * (i + 1)
                                       drawLine(
                                           hintColor,
                                           start = Offset(0f, startY),
                                           end = Offset(size.width, startY),
                                           strokeWidth = barWidthPx
                                       )
                                   }
                                   clipRect(right = size.width) {
                                       drawPath(
                                           path = path,
                                           color = lineChartColor,
                                           style = Stroke(2.dp.toPx())
                                       )
                                   }

                                   // draw highlight if use is dragging
                                   this.drawHighlight(
                                       highlightedGraphData,
                                       graphData,
                                       textMeasurer,
                                       labelStyle,
                                       lineChartColor,
                                       textColor,
                                       hintColor,
                                       offsetX,
                                       offsetY,
                                       preferCurrency
                                   )

                               }
                           })
                   } else {
                       Column(
                           modifier = Modifier
                               .padding(8.dp)
                               .aspectRatio(3 / 2f)
                               .fillMaxSize(),
                           verticalArrangement = Arrangement.Center,
                           horizontalAlignment = Alignment.CenterHorizontally
                       ) {
                           Text(
                               text = stringResource(R.string.no_enough_data_yet),
                               style = MaterialTheme.typography.titleLarge
                           )
                       }
                   }
                   // x coordinate of line chart
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Pair(startDate, endDate).toDateStringList(selectedTimeFrame).forEach { dayOfWeek ->
                            Text(
                                text = dayOfWeek,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .weight(1.0f, true),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onPrevDateRange() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.chevron_left),
                            contentDescription = "Previous date"
                        )
                    }
                    Text(
                        text = "$startDateString - $endDateString",
                        modifier = Modifier.weight(1.0f, true),
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )

                    IconButton(onClick = { onNextDateRange() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.chevron_right),
                            contentDescription = "Next date"
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .width(screenWidth.dp)
                        .height(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
                            shape = RoundedCornerShape(44.dp)
                        )
                ) {
                    TimeFrameTabIndicator(
                        widthPerItem = tabWidth.dp,
                        tabPosition = listOf(
                            0.dp,
                            tabWidth.dp,
                            (2 * tabWidth).dp,
                            (3f * tabWidth).dp
                        ),
                        tabTimeFrame = selectedTimeFrame
                    )
                    Tab {
                        TextButton(modifier = Modifier
                            .width(tabWidth.dp)
                            .height(48.dp)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(34.dp)
                            ),
                            onClick = {
                                onSelectedTime(TimeFrame.WEEKLY)
                            }
                        ) {
                            Text(
                                text = TimeFrame.WEEKLY.label,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        TextButton(modifier = Modifier
                            .width(tabWidth.dp)
                            .height(48.dp)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(34.dp)
                            ),
                            onClick = {
                                onSelectedTime(TimeFrame.MONTHLY)
                            }
                        ) {
                            Text(
                                text = TimeFrame.MONTHLY.label,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        TextButton(modifier = Modifier
                            .width(tabWidth.dp)
                            .height(48.dp)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(34.dp)
                            ),
                            onClick = {
                                onSelectedTime(TimeFrame.ANNUALLY)
                            }
                        ) {
                            Text(
                                text = TimeFrame.ANNUALLY.label,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        TextButton(modifier = Modifier
                            .width(tabWidth.dp)
                            .height(48.dp)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(34.dp)
                            ),
                            onClick = {
                                onSelectedTime(TimeFrame.ALL)
                            }
                        ) {
                            Text(
                                text = TimeFrame.ALL.label,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                }


                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.transaction),
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = PoppinsFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f, true),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.analytics, key = { it.id}) {
                        Card {
                            TransactionItem(
                                title = it.merchantName,
                                amount = it.total.toCurrency(),
                                date = it.date,
                                transactionModifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun TimeFrameTabIndicator(
    tabPosition: List<Dp>,
    tabTimeFrame: TimeFrame,
    widthPerItem: Dp
) {
    val transition = updateTransition(
        tabTimeFrame,
        label = "Time Frame indicator"
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            spring(stiffness = Spring.StiffnessLow)
        },
        label = "Indicator left"
    ) { page ->
        tabPosition[page.ordinal]
    }
//    val indicatorRight by transition.animateDp(
//        transitionSpec = {
//            spring(stiffness = Spring.StiffnessVeryLow)
//        },
//        label = "Indicator right"
//    ) { page ->
//        tabPosition[page.ordinal]
//    }
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(widthPerItem)
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(34.dp)
            )
    )

}

@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun AnalyticsPreview() {


    PersonalFinanceTheme {
        Surface(modifier = Modifier.fillMaxSize(1.0f)) {
            AnalyticsScreen(
                preferCurrency = NumberFormat.getCurrencyInstance().currency,
                uiState = AnalyticsUIState(),
                graphData = graphDataSample,
                selectedTimeFrame = TimeFrame.WEEKLY,
                onSelectedTime = { /*TODO*/ },
                tabNavigate = { /*TODO*/ },
                onNavigateAddTransaction = { /*TODO*/ }
            )
        }
    }
}