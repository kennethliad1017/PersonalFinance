package com.differentshadow.personalfinance.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.form.BudgetFormInputEvent
import com.differentshadow.personalfinance.domain.form.GoalFormInputEvent
import com.differentshadow.personalfinance.domain.form.ValidationEvent
import com.differentshadow.personalfinance.domain.model.BudgetEntity
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.utils.toCurrency
import com.differentshadow.personalfinance.utils.toDateString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate
import kotlin.math.floor


@Composable
fun BudgetProgress(
    modifier: Modifier = Modifier.fillMaxWidth(),
    currentAmount: Double,
    targetAmount: Double,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Budget Target",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f)
        )
        Text(text = currentAmount.toCurrency(), style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .weight(1f, true)
                    .height(8.dp),
                progress = (currentAmount / targetAmount).toFloat(),
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "${floor((currentAmount / targetAmount) * 100)}%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f)
            )
        }
    }
}

@Composable
fun BudgetScreen(
    budgetState: BudgetEntity,
    onNavigateBack: () -> Unit,
    onEventChanged: (event: BudgetFormInputEvent) -> Unit,
) {
    val showMoneyDialog = rememberSaveable { mutableStateOf(false) }

    val money = remember { mutableDoubleStateOf(0.0) }

    Scaffold(
        topBar = {
            Navbar(
                title = budgetState.category,
                onNavigateBack = onNavigateBack
            )
        },
        contentWindowInsets = WindowInsets(left = 16.dp, top = 24.dp, right = 16.dp, bottom = 24.dp)
    ) { contentPadding ->
        if (showMoneyDialog.value) {
            Dialog(
                onDismissRequest = {
                    showMoneyDialog.value = false
                    money.value = 0.0
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(168.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = money.value.toString(),
                        onValueChange = { t ->
                            if (!t.toDouble().isNaN()) money.value = t.toDouble() else money.value =
                                0.0
                        },
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            money.value = 0.0
                        }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = stringResource(R.string.ok))
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true)
            ) {
                BudgetProgress(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    currentAmount = budgetState.currentExpense,
                    targetAmount = budgetState.budgetLimit
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Budget Limit",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f)
                    )
                    Text(
                        text = budgetState.budgetLimit.toCurrency(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reset Frequency",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f)
                    )
                    Text(
                        text = budgetState.resetFrequency,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Spacer(Modifier.weight(1f, true))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // edit
                    IconButton(
                        onClick = { /*TODO*/ }, modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                                shape = RoundedCornerShape(32.dp)
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Edit", style = MaterialTheme.typography.bodyMedium)
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            money.value = 0.0
                            onEventChanged(BudgetFormInputEvent.onExpenseChanged(money.value.toString()))
                        }, modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                                shape = RoundedCornerShape(32.dp)
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Reset", style = MaterialTheme.typography.bodyMedium)
                }

                // delete the budget
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            onNavigateBack()
                        }, modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                                shape = RoundedCornerShape(32.dp)
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.x_mark),
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Delete", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onEventChanged(BudgetFormInputEvent.onSubmit)
                }
            ) {

                Text(text = "Add Expense")
            }

            Spacer(Modifier.height(24.dp))
        }
    }

}

@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun BudgetScreenPreview() {
    PersonalFinanceTheme {
        Surface(modifier = Modifier.fillMaxSize(1.0f)) {
            BudgetScreen(
                budgetState = BudgetEntity(
                    id = 1L,
                    category = "Utility",
                    currentExpense = 400.0,
                    budgetLimit = 1500.0,
                    isDeleted = false,
                    resetFrequency = "Monthly",
                    createdAt = LocalDate.now().toDateString("EE d, MMM yyyy")
                ),
//                validationEvents = Channel<ValidationEvent>().receiveAsFlow(),
                onEventChanged = { /*TODO*/ },
                onNavigateBack = { /*TODO*/ },
            )
        }
    }
}