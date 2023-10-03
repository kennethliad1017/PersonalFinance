package com.differentshadow.personalfinance

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.differentshadow.personalfinance.domain.form.SavingGoalForm
import com.differentshadow.personalfinance.domain.model.BillReminderUIState
import com.differentshadow.personalfinance.domain.model.NotificationUIState
import com.differentshadow.personalfinance.domain.model.SavingGoalUIState
import com.differentshadow.personalfinance.domain.model.TransactionUIState
import com.differentshadow.personalfinance.ui.components.dashboardGraph
import com.differentshadow.personalfinance.ui.components.sharedViewModel
import com.differentshadow.personalfinance.ui.screens.BillReminderFormScreen
import com.differentshadow.personalfinance.ui.screens.settings.BillReminderScreen
import com.differentshadow.personalfinance.ui.screens.ExpenseScreen
import com.differentshadow.personalfinance.ui.screens.NotificationScreen
import com.differentshadow.personalfinance.ui.screens.SavingGoalScreen
import com.differentshadow.personalfinance.ui.screens.TransactionScreen
import com.differentshadow.personalfinance.ui.screens.settings.LanguageScreen
import com.differentshadow.personalfinance.ui.screens.settings.CurrencyScreen
import com.differentshadow.personalfinance.ui.screens.settings.PreferThemeScreen
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.ui.viewmodel.BillReminderFormViewModel
import com.differentshadow.personalfinance.ui.viewmodel.BillReminderViewModel
import com.differentshadow.personalfinance.ui.viewmodel.ExpenseFormViewModel
import com.differentshadow.personalfinance.ui.viewmodel.GlobalViewModel
import com.differentshadow.personalfinance.ui.viewmodel.NotificationViewModel
import com.differentshadow.personalfinance.ui.viewmodel.SavingGoalViewModel
import com.differentshadow.personalfinance.ui.viewmodel.TransactionViewModel
import com.differentshadow.personalfinance.UserPreferences.DarkThemeConfigProto
import com.differentshadow.personalfinance.domain.model.BudgetUiState
import com.differentshadow.personalfinance.domain.model.TransactionsUIState
import com.differentshadow.personalfinance.ui.screens.BudgetScreen
import com.differentshadow.personalfinance.ui.screens.settings.AboutScreen
import com.differentshadow.personalfinance.ui.screens.settings.PrivacyPolicyScreen
import com.differentshadow.personalfinance.ui.screens.settings.TermsOfServiceScreen
import com.differentshadow.personalfinance.ui.screens.settings.TransactionHistory
import com.differentshadow.personalfinance.ui.viewmodel.BudgetViewModel
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DynamicColors.applyToActivitiesIfAvailable(this.application);

        setContent {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                color = MaterialTheme.colorScheme.background,
            ) {
                NavigationBuilprint()
            }
        }
    }
}

@Composable
fun NavigationBuilprint() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        dashboardGraph(navController)
        composable(route = "expense_form") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val viewModel = hiltViewModel<ExpenseFormViewModel>()
            val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
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
                ExpenseScreen(
                    formState = uiState,
                    validationEvents = viewModel.validationEvents,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onEventChanged = { event ->
                        viewModel.onInputChanged(event)
                    }
                )
            }
        }
        composable(route = "notification") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val viewModel = hiltViewModel<NotificationViewModel>()
            val uiState = viewModel.uiState.observeAsState(NotificationUIState()).value

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
                NotificationScreen(
                    notificationUiState = uiState,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateTo = { dest ->
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
        composable(route = "saving") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val viewModel = hiltViewModel<SavingGoalViewModel>()
            val uiState = viewModel.uiState.observeAsState(SavingGoalUIState()).value
            val formState = viewModel.formState.collectAsStateWithLifecycle(
                SavingGoalForm()
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
                SavingGoalScreen(
                    savingGoalUiState = uiState,
                    formState = formState,
                    validationEvents = viewModel.validationEvents,
                    onInputChanged = { event ->
                        viewModel.onInputChange(event)
                    },
                    onItemNavigate = { dest ->
                        navController.navigate(dest) {
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                )
            }
        }

        composable(
            "transaction/{transactionId}",
            arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
        ) { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val viewModel = hiltViewModel<TransactionViewModel>()

            val transactionId = entry.arguments?.getLong("transactionId")

            transactionId?.let {
                viewModel.getTransactionId(it)
            }
            // Observe the transactionUIState
            val uiState by viewModel.transactionUIState.observeAsState(TransactionUIState())


            // Check if the transaction data is available before showing the TransactionScreen
            uiState.transaction?.let {
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
                    TransactionScreen(uiState = uiState, popUp = {
                        navController.popBackStack()
                    })
                }
            }


        }
        composable(
            route = "saving/{savingId}",
            arguments = listOf(navArgument("savingId") { type = NavType.LongType })
        ) { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val savingGoalId = entry.arguments?.getLong("savingId")!!


            val viewModel = hiltViewModel<SavingGoalViewModel>()

            viewModel.selectedGoal(savingGoalId)

            val uiState = viewModel.uiState.observeAsState(initial = SavingGoalUIState()).value

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
                SavingGoalScreen(
                    uiState = uiState,
                    validationEvents = viewModel.validationEvents,
                    onEventChanged = {
                        viewModel.onInputChange(it)
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("billreminder") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val billReminderViewModel = hiltViewModel<BillReminderViewModel>()
            val uiState = billReminderViewModel.uiState.observeAsState(BillReminderUIState()).value

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
                BillReminderScreen(
                    reminderState = uiState,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateTo = {
                        navController.navigate(it) {
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

        composable("billreminder_form") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val viewModel = hiltViewModel<BillReminderFormViewModel>()

            val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

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
                BillReminderFormScreen(
                    formState = uiState,
                    validationEvents = viewModel.validationEvents,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onEventChanged = { event ->
                        viewModel.onInputChanged(event)
                    }
                )
            }
        }

        composable("theme") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val themeMode =
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
                PreferThemeScreen(
                    themeMode = themeMode,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onThemeChanged = {
                        globalViewModel.onThemeChanged(it)
                    }
                )
            }

        }

        composable("currency") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val currentCurrency =
                globalViewModel.preferCurrency.collectAsStateWithLifecycle(initialValue = NumberFormat.getCurrencyInstance(
                    Locale.getDefault()).currency).value

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
                CurrencyScreen(
                    preferCurrency = currentCurrency,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onCurrencyChanged = {
                        globalViewModel.onCurrencyChanged(it)
                    },
                )
            }
        }

        composable("language") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value

            val currentLanguage = globalViewModel.preferLanguage.collectAsStateWithLifecycle(
                "English"
            ).value


            val supportedLanguages = mapOf(
                R.string.en to "en-US",
                R.string.de to "de",
                R.string.es to "es",
                R.string.ja to "ja",
                R.string.ko to "ko"
            ).mapKeys { stringResource(it.key) }


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
                LanguageScreen(
                    preferLanguage = currentLanguage,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onLanguageChanged = {
                        // set app locale given the user's selected locale
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags(
                                supportedLanguages[it]
                            )
                        )
                        globalViewModel.onLanguageChanged(it)
                    }
                )
            }
        }

        composable("terms-of-service") { entry ->
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
                TermsOfServiceScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

        }

        composable("privacy-policy") { entry ->
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
                PrivacyPolicyScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

        }

        composable("transaction_history") { entry ->
            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val selectedDate = globalViewModel.selectedDate.observeAsState(initial = LocalDate.now()).value
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value


            val currentCurrency = globalViewModel.preferCurrency.collectAsStateWithLifecycle(
                NumberFormat.getCurrencyInstance(Locale.getDefault()).currency
            ).value
            
            val transactionViewModel = hiltViewModel<TransactionViewModel>()

            transactionViewModel.getTransactions(selectedDate)
            
            val transactions = transactionViewModel.uiState.observeAsState(initial = TransactionsUIState()).value

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
                TransactionHistory(
                    uiState = transactions,
                    preferCurrency = currentCurrency,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateTo = {dest ->
                        navController.navigate(dest)
                    }
                )
            }

        }

        composable("about") {entry ->
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
                AboutScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }


        }

        composable("budget/{budgetId}",
                arguments = listOf(navArgument("budgetId") { type = NavType.LongType })) {entry ->
            val budgetId = entry.arguments?.getLong("budgetId")

            val globalViewModel = entry.sharedViewModel<GlobalViewModel>(navController)
            val preferTheme =
                globalViewModel.preferTheme.collectAsStateWithLifecycle(initialValue = DarkThemeConfigProto.SYSTEM_DEFAULT).value


            val viewModel = hiltViewModel<BudgetViewModel>()
            budgetId?.let {id ->
                viewModel.getBudget(id)
            }
            // show snackbar error if it has error
            val uiState = viewModel.uiState.observeAsState(initial = BudgetUiState())

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
                uiState.value.budgetEntity?.let {budget ->
                    BudgetScreen(
                        budgetState = budget,
                        onNavigateBack = { navController.popBackStack() },
                        onEventChanged = { /*TODO*/ }
                    )
                }
            }
        }
    }
}

//@Composable
//fun ColorBottomNav() {
//    var selectedItem by remember { mutableIntStateOf(0) }
//    var prevSelectedIndex by remember { mutableIntStateOf(0) }
//
//    AnimatedNavigationBar(
//        modifier = Modifier
//            .padding(horizontal = 8.dp, vertical = 60.dp)
//            .height(85.dp),
//        selectedIndex = selectedItem,
//        ballColor = Color.White,
//        cornerRadius = shapeCornerRadius(25.dp),
//        ballAnimation = Straight(
//            spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessVeryLow)
//        ),
//        indentAnimation = StraightIndent(
//            indentWidth = 64.dp,
//            indentHeight = 32.dp,
//            animationSpec = tween(1000)
//        )
//    ) {
//        colorButtons.forEachIndexed { index, it ->
//            ColorButton(
//                modifier = Modifier.fillMaxSize(),
//                prevSelectedIndex = prevSelectedIndex,
//                selectedIndex = selectedItem,
//                index = index,
//                onClick = {
//                    prevSelectedIndex = selectedItem
//                    selectedItem = index
//                },
//                icon = it.icon,
//                contentDescription = stringResource(id = it.description),
//                animationType = it.animationType,
//                background = it.animationType.background
//            )
//        }
//    }
//}