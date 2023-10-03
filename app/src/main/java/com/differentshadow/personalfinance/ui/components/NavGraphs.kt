package com.differentshadow.personalfinance.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.UserPreferences.DarkThemeConfigProto
import com.differentshadow.personalfinance.domain.form.BudgetForm
import com.differentshadow.personalfinance.domain.model.AnalyticsUIState
import com.differentshadow.personalfinance.domain.model.BudgetUiState
import com.differentshadow.personalfinance.domain.model.Expense
import com.differentshadow.personalfinance.domain.model.HomeUiState
import com.differentshadow.personalfinance.ui.screens.tabs.AnalyticsScreen
import com.differentshadow.personalfinance.ui.screens.tabs.BudgetScreen
import com.differentshadow.personalfinance.ui.screens.tabs.HomeScreen
import com.differentshadow.personalfinance.ui.screens.tabs.SettingScreen
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.utils.TimeFrame
import com.differentshadow.personalfinance.utils.getDayWeekStart
import com.differentshadow.personalfinance.ui.viewmodel.AnalyticsViewModel
import com.differentshadow.personalfinance.ui.viewmodel.BudgetViewModel
import com.differentshadow.personalfinance.ui.viewmodel.GlobalViewModel
import com.differentshadow.personalfinance.ui.viewmodel.HomeViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale


sealed class Screen(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    object Dashboard : Screen("dashboard", R.string.Dashboard, icon = R.drawable.outline_home)
    object Analytics : Screen("analytics", R.string.Analytics, icon = R.drawable.chart_pie_simple)
    object Create : Screen("expense_form", R.string.Create, icon = R.drawable.square_plus)
    object BudgetTracker : Screen("budget", R.string.Budget, icon = R.drawable.wallet)
    object Settings : Screen("settings", R.string.Settings, icon = R.drawable.gear)
}


val items = listOf(
    Screen.Dashboard,
    Screen.Analytics,
    Screen.Create,
    Screen.BudgetTracker,
    Screen.Settings
)

fun NavGraphBuilder.dashboardGraph(navController: NavController) {
    navigation(startDestination = "dashboard", route = "home") {
        composable("dashboard") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val selectedDate =
                globalViewModel.selectedDate.observeAsState(initial = LocalDate.now()).value
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val viewModel = hiltViewModel<HomeViewModel>()
            val uiState = viewModel.uiState.collectAsStateWithLifecycle(HomeUiState()).value

            viewModel.setSelectedDate(selectedDate)

            val visibleDates =
                globalViewModel.calendarUiModel.observeAsState(initial = globalViewModel.getData()).value.visibleDates


            val currentCurrency = globalViewModel.preferCurrency.collectAsStateWithLifecycle(
                NumberFormat.getCurrencyInstance(Locale.getDefault()).currency
            ).value


            PersonalFinanceTheme(
                darkTheme = when (preferTheme) {
                    DarkThemeConfigProto.LIGHT -> {
                        false
                    }

                    DarkThemeConfigProto.DARK -> {
                        true
                    }

                    else -> {
                        isSystemInDarkTheme()
                    }
                }
            ) {
                HomeScreen(
                    uiState = uiState,
                    preferCurrency = currentCurrency,
                    selectedDate = selectedDate,
                    visibleDates = visibleDates,
                    onSelectedDateChanged = { date ->
                        globalViewModel.onSelectedDateChanged(date)
                        viewModel.setSelectedDate(date.date)
                    },
                    onNextDate = {
                        globalViewModel.onNextDate()
                    },
                    onPrevDate = {
                        globalViewModel.onPrevDate()
                    },
                    onNavigateToSaving = { id ->
                        navController.navigate("saving/$id")
                    },
                    onNavigateRoute = { dest ->
                        navController.navigate(dest)
                    },
                    tabNavigate = { dest ->
                        navController.navigate(dest) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
        composable("analytics") { entry ->
            val initialDate = LocalDate.now().getDayWeekStart(DayOfWeek.SUNDAY)
            val viewmodel = hiltViewModel<AnalyticsViewModel>()

            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val selectedDate =
                globalViewModel.selectedDate.observeAsState(initial = LocalDate.now()).value
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val currentCurrency = globalViewModel.preferCurrency.collectAsStateWithLifecycle(
                NumberFormat.getCurrencyInstance(Locale.getDefault()).currency
            ).value

            viewmodel.setSelectedDate(selectedDate)

            val uiState = viewmodel.uiState.collectAsStateWithLifecycle(
                initialValue = AnalyticsUIState()
            ).value

            val analyticsUIState = viewmodel.analyticsUIState.collectAsStateWithLifecycle(
                initialValue = listOf()
            ).value

            val selectedTimeFrame =
                viewmodel.selectedTimeFrame.collectAsStateWithLifecycle(TimeFrame.WEEKLY).value


            val (startDate, endDate) = viewmodel.dateRange.collectAsStateWithLifecycle(
                initialValue = initialDate to initialDate.plusDays(
                    6
                )
            ).value


            PersonalFinanceTheme(
                darkTheme = when (preferTheme) {
                    DarkThemeConfigProto.LIGHT -> {
                        false
                    }

                    DarkThemeConfigProto.DARK -> {
                        true
                    }

                    else -> {
                        isSystemInDarkTheme()
                    }
                }
            ) {
                AnalyticsScreen(
                    preferCurrency = currentCurrency,
                    startDate = startDate,
                    endDate = endDate,
                    uiState = uiState,
                    graphData = analyticsUIState,
                    selectedTimeFrame = selectedTimeFrame,
                    onNextDateRange = {
                        viewmodel.onNextDateRange()
                    },
                    onPrevDateRange = {
                        viewmodel.onPrevDateRange()
                    },
                    onSelectedTime = { timeFrame ->
                        viewmodel.setSelectedTimeFrame(timeFrame)
                    },
                    tabNavigate = { dest ->
                        navController.navigate(dest) {
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    onNavigateAddTransaction = {
                        navController.navigate("expense_form") {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    })
            }
        }
        composable("budget") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val currentCurrency = globalViewModel.preferCurrency.collectAsStateWithLifecycle(
                NumberFormat.getCurrencyInstance(Locale.getDefault()).currency
            ).value


            val budgetViewModel = hiltViewModel<BudgetViewModel>()
            val budgetStates = budgetViewModel.uiState.observeAsState(BudgetUiState()).value
            val formState =
                budgetViewModel.formState.collectAsStateWithLifecycle(BudgetForm()).value

            val donutGraph = budgetViewModel.donutChartState.collectAsStateWithLifecycle(
                initialValue = listOf()
            ).value

            PersonalFinanceTheme(
                darkTheme = when (preferTheme) {
                    DarkThemeConfigProto.LIGHT -> {
                        false
                    }

                    DarkThemeConfigProto.DARK -> {
                        true
                    }

                    else -> {
                        isSystemInDarkTheme()
                    }
                }
            ) {
                BudgetScreen(
                    preferCurrency = currentCurrency,
                    uiState = budgetStates,
                    formState = formState,
                    donutGraph = donutGraph,
                    validationEvents = budgetViewModel.validationEvents,
                    onEventChanged = { event ->
                        budgetViewModel.onInputChanged(event)
                    },
                    onNavigateToBudget = { param ->
                        navController.findDestination("budget/$param")
                    },
                    tabNavigate = { dest ->
                        navController.navigate(dest) {
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    })
            }

        }
        composable("settings") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            PersonalFinanceTheme(
                darkTheme = when (preferTheme) {
                    DarkThemeConfigProto.LIGHT -> {
                        false
                    }

                    DarkThemeConfigProto.DARK -> {
                        true
                    }

                    else -> {
                        isSystemInDarkTheme()
                    }
                }
            ) {
                SettingScreen(
                    onNavigate = { dest ->
                        navController.navigate(dest) {
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    tabNavigate = { dest ->
                        navController.navigate(dest) {
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}