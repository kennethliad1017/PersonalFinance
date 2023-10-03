package com.differentshadow.personalfinance.ui.screens.tabs

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.form.BudgetForm
import com.differentshadow.personalfinance.domain.form.BudgetFormInputEvent
import com.differentshadow.personalfinance.domain.form.ValidationEvent
import com.differentshadow.personalfinance.domain.model.BudgetEntity
import com.differentshadow.personalfinance.domain.model.BudgetUiState
import com.differentshadow.personalfinance.domain.model.DonutChartData
import com.differentshadow.personalfinance.domain.model.DonutChartDataCollection
import com.differentshadow.personalfinance.ui.components.DonutChart
import com.differentshadow.personalfinance.ui.components.items
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.ui.theme.PoppinsFamily
import com.differentshadow.personalfinance.utils.BudgetResetFreqs
import com.differentshadow.personalfinance.utils.CategoryList
import com.differentshadow.personalfinance.utils.toCurrency
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency
import kotlin.math.abs


@Composable
fun AppBar(onShowForm: () -> Unit) {
    Row(
        modifier = Modifier
            .height(72.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.0f, true)) {
            Text(
                stringResource(R.string.budget_tracker),
                style = MaterialTheme.typography.titleLarge,
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 26.sp
            )
            Text(
                stringResource(R.string.budget_description),
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Normal,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }


        Surface(
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .shadow(4.dp)
        ) {
            IconButton(
                onClick = { onShowForm() }, modifier = Modifier
                    .size(44.dp)
                    .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun Progressbar(progress: Float, currentAmount: String, budgetLimit: String, screenWidth: Float) {

    Box {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            trackColor = MaterialTheme.colorScheme.surfaceColorAtElevation(24.dp),
            strokeCap = StrokeCap.Round
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .offset(0.dp, 0.dp)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentAmount,
                style = MaterialTheme.typography.bodyLarge,
                color = if (progress >= 0.056f) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = budgetLimit,
                style = MaterialTheme.typography.bodyLarge,
                color = if (progress >= 0.92f) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun BudgetItem(
    preferCurrency: Currency,
    progress: Float,
    currentAmount: Double,
    budgetLimit: Double,
    category: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    budgetLimitXPos: Float
) {

    Card(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = category, style = MaterialTheme.typography.bodyLarge)
                if (progress <= 1.0f) {
                    Text(
                        text = stringResource(
                            R.string.remaining,
                            "${preferCurrency.symbol}${(abs(budgetLimit - currentAmount)).toCurrency()}"
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.overspent,
                            (abs(budgetLimit - currentAmount)).toCurrency()
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Progressbar(
                progress = progress,
                currentAmount = currentAmount.toCurrency(),
                budgetLimit = budgetLimit.toCurrency(),
                screenWidth = budgetLimitXPos
            )

            Spacer(Modifier.height(12.dp))

            if (currentAmount > 0.0) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when {
                        progress < 0.6 -> {
                            Text(
                                text = "Great job! You're on track with your budget",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        progress >= 0.6 -> {
                            Text(
                                text = "Almost there! Be mindful to stay within budget",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        progress == 1f -> {
                            Text(
                                text = "Oops! You've reach your budget!",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        progress > 1f -> {
                            Text(
                                text = "Oops! You've exceeded your budget!",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}


val data = listOf(
    DonutChartData(150.0f, Color(0xFF8BC34A), "Grocery"),
    DonutChartData(50.0f, Color(0xFF2196F3), "Dining"),
    DonutChartData(80.0f, Color(0xFF9C27B0), "Transportation"),
    DonutChartData(120.0f, Color(0xFF009688), "Utility"),
    DonutChartData(200.0f, Color(0xFF795548), "Rent/Mortgage"),
    DonutChartData(70.0f, Color(0xFFE91E63), "Entertainment"),
    DonutChartData(60.0f, Color(0xFFCDDC39), "Health"),
    DonutChartData(90.0f, Color(0xFFFF9800), "Fitness"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    preferCurrency: Currency,
    uiState: BudgetUiState,
    formState: BudgetForm,
    donutGraph: List<DonutChartData> = listOf(),
    onEventChanged: (event: BudgetFormInputEvent) -> Unit,
    validationEvents: Flow<ValidationEvent>,
    onNavigateToBudget: (id: Long) -> Unit,
    tabNavigate: (dest: String) -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp - 32
    val tabItemWidth = configuration.screenWidthDp / 5f

    val selectedIndex = remember { mutableStateOf(-1) }

    var expanded by remember { mutableStateOf(false) }
    var resetFrequentExpanded by remember {
        mutableStateOf(false)
    }

    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Budget has been added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    showBottomSheet = false
                }

                else -> {}
            }

        }
    }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding(),
        topBar = { AppBar(onShowForm = { scope.launch { showBottomSheet = true } }) },
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp),
        bottomBar = {
            BottomAppBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = index == 3,
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
        }
    ) { contentPadding ->
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(stringResource(R.string.category))
                    Spacer(modifier = Modifier.height(4.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        TextField(
                            value = formState.category,
                            onValueChange = {},
                            readOnly = true,
                            isError = formState.categoryErrorMsg != null,
                            supportingText = {
                                if (formState.categoryErrorMsg != null) {
                                    Text(text = formState.categoryErrorMsg)
                                }
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                                errorIndicatorColor = Color.Transparent
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            CategoryList.forEach { categoryName ->
                                DropdownMenuItem(
                                    text = { Text(text = categoryName) },
                                    onClick = {
                                        onEventChanged(
                                            BudgetFormInputEvent.onCategoryChanged(
                                                categoryName
                                            )
                                        )
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(stringResource(R.string.budget_limit))
                    Spacer(modifier = Modifier.height(4.dp))
                    TextField(
                        value = formState.budgetLimit,
                        onValueChange = {
                            onEventChanged(
                                BudgetFormInputEvent.onBudgetLimitChanged(
                                    it
                                )
                            )
                        },
                        isError = formState.budgetLimitErrorMsg != null,
                        supportingText = {
                            if (formState.budgetLimitErrorMsg != null) {
                                Text(text = formState.budgetLimitErrorMsg)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                            errorIndicatorColor = Color.Transparent
                        )
                    )
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(stringResource(R.string.budget_reset_period))
                    Spacer(modifier = Modifier.height(4.dp))
                    ExposedDropdownMenuBox(
                        expanded = resetFrequentExpanded,
                        onExpandedChange = {
                            resetFrequentExpanded = !resetFrequentExpanded
                        }
                    ) {
                        TextField(
                            value = formState.resetFrequent,
                            onValueChange = {},
                            readOnly = true,
                            isError = formState.resetFreqErrorMsg != null,
                            supportingText = {
                                if (formState.resetFreqErrorMsg != null) {
                                    Text(text = formState.resetFreqErrorMsg)
                                }
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = resetFrequentExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                                errorIndicatorColor = Color.Transparent
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = resetFrequentExpanded,
                            onDismissRequest = { resetFrequentExpanded = false }
                        ) {
                            BudgetResetFreqs.forEach { resetFrequent ->
                                DropdownMenuItem(
                                    text = { Text(text = resetFrequent) },
                                    onClick = {
                                        onEventChanged(
                                            BudgetFormInputEvent.onResetFrequentChanged(
                                                resetFrequent
                                            )
                                        )
                                        resetFrequentExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        onEventChanged(BudgetFormInputEvent.onSubmit)
                    }
                ) {
                    Text(text = stringResource(R.string.submit))
                }

                Spacer(Modifier.height(24.dp))
            }
        }

        if (uiState.budgets.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Image(
                    modifier = Modifier
                        .width(screenWidth.dp)
                        .aspectRatio(
                            6f / 4f,
                            true,
                        ),
                    painter = painterResource(id = R.drawable.manage_money_pana),
                    contentDescription = "Budget Illustration"
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.let_s_get_organized_set_your_budget),
                    fontFamily = PoppinsFamily,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Spacer(Modifier.height(16.dp))
                DonutChart(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    chartSize = (screenWidth * 0.5128).dp,
                    index = selectedIndex.value,
                    inactiveStrokeWidth = 16.dp,
                    activeStrokeWidth = 34.dp,
                    gapPercentage = 0.44f,
                    onSelectedChange = { index ->
                        selectedIndex.value = index
                    },
                    data = DonutChartDataCollection(
                        items = donutGraph
                    ),
                )
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.budgets, key = { it.id }) { budget ->
                        val progress = (budget.currentExpense / budget.budgetLimit).toFloat()
                        val progressAnim: Float by animateFloatAsState(
                            progress,
                            label = "progress budget"
                        )

                        BudgetItem(
                            modifier = Modifier.fillMaxWidth().clickable {
                                onNavigateToBudget(budget.id)
                            },
                            preferCurrency = preferCurrency,
                            progress = progressAnim,
                            currentAmount = budget.currentExpense,
                            budgetLimit = budget.budgetLimit,
                            category = budget.category,
                            budgetLimitXPos = screenWidth * 0.71f
                        )
                    }
                }


            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun BudgetPreview() {
    PersonalFinanceTheme {
        Surface(modifier = Modifier.fillMaxSize(1.0f)) {
            BudgetScreen(
                preferCurrency = NumberFormat.getCurrencyInstance().currency,
                uiState = BudgetUiState(
                    budgets = listOf(
                        BudgetEntity(
                            id = 1L,
                            category = "Grocery",
                            currentExpense = 868.00,
                            budgetLimit = 1000.00,
                            isDeleted = false,
                            resetFrequency = "monthly",
                            createdAt = "Mon 28, Aug 2023",
                        ),
                        BudgetEntity(
                            id = 2L,
                            category = "Dining",
                            currentExpense = 200.00,
                            budgetLimit = 700.00,
                            isDeleted = false,
                            resetFrequency = "monthly",
                            createdAt = "Fri 18, Aug 2023",
                        ),
                        BudgetEntity(
                            id = 3L,
                            category = "Transportation",
                            currentExpense = 100.00,
                            budgetLimit = 400.00,
                            isDeleted = false,
                            resetFrequency = "monthly",
                            createdAt = "Fri 18, Aug 2023",
                        )
                    )
                ),
                donutGraph = data,
                formState = BudgetForm(),
                validationEvents = Channel<ValidationEvent>().receiveAsFlow(),
                onNavigateToBudget = { /*TODO*/ },
                tabNavigate = { /*TODO*/ },
                onEventChanged = { /*TODO*/ }
            )
        }
    }
}