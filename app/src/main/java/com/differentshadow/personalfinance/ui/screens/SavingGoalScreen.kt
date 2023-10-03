package com.differentshadow.personalfinance.ui.screens

import android.icu.util.LocaleData
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.window.DialogProperties
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.form.GoalFormInputEvent
import com.differentshadow.personalfinance.domain.form.ValidationEvent
import com.differentshadow.personalfinance.domain.model.SavingGoalEntity
import com.differentshadow.personalfinance.domain.model.SavingGoalUIState
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.utils.toCurrency
import com.differentshadow.personalfinance.utils.toDateString
import com.differentshadow.personalfinance.utils.toLong
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate
import kotlin.math.floor


@Composable
fun GoalProgress(
    modifier: Modifier = Modifier.fillMaxWidth(),
    currentAmount: Double,
    targetAmount: Double,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "My Target", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f))
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
            Text(text = "${floor((currentAmount / targetAmount) * 100)}%", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingGoalScreen(
    uiState: SavingGoalUIState,
    validationEvents: Flow<ValidationEvent>,
    onEventChanged: (event: GoalFormInputEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val showMoneyDialog = rememberSaveable { mutableStateOf(false) }

    val money = remember { mutableDoubleStateOf(0.0) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.savingGoal?.goalDate?.toLong("EEE d, MMM yyyy") ?: LocalDate.now().toLong(),
        initialDisplayMode = DisplayMode.Picker
    )

    Scaffold(
        topBar = {
            Navbar(
                title = uiState.savingGoal?.title ?: stringResource(id = R.string.saving_goals),
                onNavigateBack = onNavigateBack
            )
        },
        contentWindowInsets = WindowInsets(left = 16.dp, top = 24.dp, right = 16.dp, bottom = 24.dp)
    ) {contentPadding ->
        if (showDialog.value) {
            DatePickerDialog(
                onDismissRequest = { showDialog.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { onEventChanged(GoalFormInputEvent.onGoalDateChanged(it)) }
                        showDialog.value = false
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
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
                        .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = money.value.toString(),
                        onValueChange = { t -> if (!t.toDouble().isNaN())  money.value = t.toDouble()  },
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
                GoalProgress(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    currentAmount = uiState.savingGoal?.currentSaving ?: 0.0,
                    targetAmount =  uiState.savingGoal?.goalSaving ?: 0.0
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Category", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f))
                    Text(text = "${uiState.savingGoal?.category}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium,)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "My Target", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f))
                    Text(text = uiState.savingGoal?.goalSaving!!.toCurrency(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium,)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Withdrawal date", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f))
                    Text(text = uiState.savingGoal?.goalDate ?: LocalDate.now().toDateString("EE d, MMM yyyy"), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium,)
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
                   IconButton(onClick = { /*TODO*/ }, modifier = Modifier
                       .size(56.dp)
                       .background(
                           color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                           shape = RoundedCornerShape(32.dp)
                       )) {
                       Icon(painter = painterResource(id = R.drawable.check), contentDescription = null)
                   }
                   Spacer(Modifier.height(4.dp))
                   Text(text = "Completed", style = MaterialTheme.typography.bodyMedium)
               }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                            shape = RoundedCornerShape(32.dp)
                        )) {
                        Icon(painter = painterResource(id = R.drawable.calendar), contentDescription = null)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Extend", style = MaterialTheme.typography.bodyMedium)
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                            shape = RoundedCornerShape(32.dp)
                        )) {
                        Icon(painter = painterResource(id = R.drawable.x_mark), contentDescription = null)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Failed", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onEventChanged(GoalFormInputEvent.onSubmit)
                }
            ) {

                Text(text = "Add Money")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun SavingGoalScreenPreview() {
    PersonalFinanceTheme {
        Surface(modifier = Modifier.fillMaxSize(1.0f)) {
            SavingGoalScreen(
                uiState = SavingGoalUIState(savingGoal = SavingGoalEntity(
                    id = 1L,
                    title = "Buy Laptop",
                    category = "Utility",
                    currentSaving = 12000.0,
                    goalSaving = 250000.0,
                    goalDate = "Tue 12, Sep 2026",
                    isDeleted = false,
                    createdAt = LocalDate.now().toDateString("EE d, MMM yyyy")
                )),
                validationEvents = Channel<ValidationEvent>().receiveAsFlow(),
                onEventChanged = { /*TODO*/ },
                onNavigateBack = { /*TODO*/ },
            )
        }
    }
}