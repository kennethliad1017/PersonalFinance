package com.differentshadow.personalfinance.ui.screens.tabs

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.model.HomeUiState
import com.differentshadow.personalfinance.domain.model.SavingGoalEntity
import com.differentshadow.personalfinance.ui.colorButtons
import com.differentshadow.personalfinance.ui.components.items
import com.differentshadow.personalfinance.ui.customanimation.AnimatedNavigationBar
import com.differentshadow.personalfinance.ui.customanimation.animation.balltrajectory.Straight
import com.differentshadow.personalfinance.ui.customanimation.animation.indentshape.StraightIndent
import com.differentshadow.personalfinance.ui.customanimation.animation.indentshape.shapeCornerRadius
import com.differentshadow.personalfinance.ui.customanimation.colorbuttons.ColorButton
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.ui.theme.PoppinsFamily
import com.differentshadow.personalfinance.utils.CalendarUiModel
import com.differentshadow.personalfinance.utils.Categories
import com.differentshadow.personalfinance.utils.toCurrency
import com.differentshadow.personalfinance.utils.toDateString
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Currency


@Composable
fun ExpenseCard(title: String, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title, fontSize = 16.sp,
            )
            Text(
                text = text, fontSize = 44.sp,
            )
        }
    }
}

@Composable
fun HeaderTitle(
    linkModifier: Modifier = Modifier,
    title: String,
    linkText: String? = null,
    titleFontSize: TextUnit = TextUnit.Unspecified,
    linkTextFontSize: TextUnit = TextUnit.Unspecified,
    titleColor: Color? = Color.Unspecified,
    linkTextColor: Color? = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (titleColor != null) {
            Text(
                text = title,
                fontSize = titleFontSize,
                color = titleColor,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (linkText != null && linkTextColor != null) {
            Text(
                text = linkText,
                fontSize = linkTextFontSize,
                color = linkTextColor,
                modifier = linkModifier
            )
        }
    }
}

@Composable
fun SavingItem(
    modifier: Modifier = Modifier,
    title: String,
    currentAmount: Double,
    goalAmount: Double
) {


    Column(modifier = modifier) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = currentAmount.toCurrency(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "/", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = goalAmount.toCurrency(), fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = currentAmount.toFloat() / goalAmount.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(16.dp)),
            trackColor = MaterialTheme.colorScheme.surfaceColorAtElevation(24.dp)
        )
    }
}

@Composable
fun ColorBottomNav() {
    var selectedItem by remember { mutableStateOf(0) }
    var prevSelectedIndex by remember { mutableStateOf(0) }

    AnimatedNavigationBar(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 24.dp)
            .height(85.dp),
        selectedIndex = selectedItem,
        ballColor = Color.White,
        cornerRadius = shapeCornerRadius(25.dp),
        ballAnimation = Straight(
            spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessVeryLow)
        ),
        indentAnimation = StraightIndent(
            indentWidth = 64.dp,
            indentHeight = 32.dp,
            animationSpec = tween(1000)
        )
    ) {
        colorButtons.forEachIndexed { index, it ->
            ColorButton(
                modifier = Modifier.fillMaxSize(),
                prevSelectedIndex = prevSelectedIndex,
                selectedIndex = selectedItem,
                index = index,
                onClick = {
                    prevSelectedIndex = selectedItem
                    selectedItem = index
                },
                icon = it.icon,
                contentDescription = stringResource(id = it.description),
                animationType = it.animationType,
                background = it.animationType.background
            )
        }
    }
}

@Composable
fun Content(data: List<CalendarUiModel.Date>, onDateClick: (CalendarUiModel.Date) -> Unit) {
    LazyRow {
        items(items = data) { date ->
            DateContent(date = date, onSelected = onDateClick)
        }
    }
}

@Composable
fun DateContent(date: CalendarUiModel.Date, onSelected: (CalendarUiModel.Date) -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable { // making the element clickable, by adding 'clickable' modifier
                onSelected(date)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (date.isSelected) MaterialTheme.colorScheme.tertiary else {
                MaterialTheme.colorScheme.tertiaryContainer
            }
        ),
    ) {
        Column(
            modifier = Modifier
                .width(48.dp)
                .height(64.dp)
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
        }
    }
}


@Composable
fun HomeScreen(
    uiState: HomeUiState,
    preferCurrency: Currency,
    onNavigateToSaving: (savingId: Long) -> Unit,
    onNavigateRoute: (route: String) -> Unit,
    tabNavigate: (route: String) -> Unit,
    selectedDate: LocalDate = LocalDate.now(),
    visibleDates: List<CalendarUiModel.Date> = listOf(),
    onSelectedDateChanged: (date: CalendarUiModel.Date) -> Unit = {},
    onNextDate: () -> Unit = {},
    onPrevDate: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current;
    val screenWidth = configuration.screenWidthDp * 0.56f
    val tabItemWidth = configuration.screenWidthDp / 5f


    Scaffold(
        modifier = Modifier
            .systemBarsPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1.0f)
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(1.0f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = { onNavigateRoute("notification") },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_bell),
                            contentDescription = stringResource(R.string.notification)
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    items.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            selected = index == 0,
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
//                        IconButton(
//                            onClick = { tabNavigate(screen.route) }, modifier = Modifier
//                                .width(tabItemWidth.dp)
//                                .fillMaxHeight()
//                        ) {
//                            if (index == 2) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.rounded_rect),
//                                    contentDescription = null,
//                                )
//                            }
//
//                            if (index == 0) {
//                                Icon(
//                                    painterResource(id = screen.icon),
//                                    contentDescription = "Localized description",
//                                    tint = MaterialTheme.colorScheme.primary
//                                )
//                            }
//                            Icon(
//                                painterResource(id = screen.icon),
//                                contentDescription = "Localized description"
//                            )
//                        }
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp, top = 0.dp, bottom = 0.dp),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            ExpenseCard(
                title = stringResource(id = R.string.today_s_spending),
                text = uiState.currentExpenses.toCurrency(preferCurrency), cardModifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1.0f, true),
                        text = selectedDate.toDateString("EEE d, MMM yyyy"),
                        fontFamily = PoppinsFamily,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    IconButton(onClick = {
                        onPrevDate()

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.chevron_left),
                            contentDescription = "Previous"
                        )
                    }
                    IconButton(onClick = {
                        onNextDate()

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.chevron_right),
                            contentDescription = "Next"
                        )
                    }
                }
                Content(data = visibleDates, onDateClick = { date ->
                    onSelectedDateChanged(date)
                })

            }


            Spacer(modifier = Modifier.height(24.dp))


            HeaderTitle(
                title = stringResource(R.string.savings),
                titleFontSize = 18.sp,
                linkText = stringResource(R.string.see_all),
                modifier = Modifier
                    .fillMaxWidth(), linkTextColor = MaterialTheme.colorScheme.primary,
                linkModifier = Modifier.clickable {
                    onNavigateRoute("saving")
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f, true)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    if (uiState.overviewGoals.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = stringResource(R.string.get_started_desc),
                                fontFamily = PoppinsFamily,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            TextButton(onClick = { onNavigateRoute("saving") }) {
                                Text(text = "Add Goal")
                            }
                        }
                    }
                }

                items(uiState.overviewGoals, key = { it.id }) { goals ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onNavigateToSaving(9012L)
                            },
                        colors = CardDefaults.elevatedCardColors(),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        SavingItem(
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 12.dp),
                            title = goals.title,
                            currentAmount = goals.currentSaving,
                            goalAmount = goals.goalSaving
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun HomePreview() {
    PersonalFinanceTheme {
        Surface(modifier = Modifier.fillMaxSize(1.0f)) {
            HomeScreen(
                uiState = HomeUiState(
                    overviewGoals = listOf(
                        SavingGoalEntity(
                            id = 1L,
                            title = "Travel to Europe",
                            category = Categories.Travel.title,
                            currentSaving = 2000.0,
                            goalSaving = 12000.0,
                            goalDate = "Fri 14, Jun 2024",
                            isDeleted = false,
                            createdAt = "Fri 15, Sep 2023"
                        ),
                        SavingGoalEntity(
                            id = 2L,
                            title = "Buy Car",
                            category = Categories.Transportation.title,
                            currentSaving = 7000.0,
                            goalSaving = 32000.0,
                            goalDate = "Sat 17, Oct 2026",
                            isDeleted = false,
                            createdAt = "Fri 15, Sep 2023"
                        )
                    )
                ),
                preferCurrency = NumberFormat.getCurrencyInstance().currency,
                onNavigateToSaving = { /*TODO*/ },
                tabNavigate = { /*TODO*/ },
                onNavigateRoute = { /*TODO*/ }
            )

        }
    }
}