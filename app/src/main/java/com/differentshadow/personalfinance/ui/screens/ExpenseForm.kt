package com.differentshadow.personalfinance.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.form.ExpenseFormInputEvent
import com.differentshadow.personalfinance.domain.form.TransactionForm
import com.differentshadow.personalfinance.domain.form.ValidationEvent
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.utils.CategoryList
import com.differentshadow.personalfinance.utils.toDateString
import com.differentshadow.personalfinance.utils.toDateTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Calendar


@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("OK") }
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(text = message)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    formState: TransactionForm,
    validationEvents: Flow<ValidationEvent>,
    onEventChanged: (event: ExpenseFormInputEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    
    LaunchedEffect(key1 = context) {
        validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Transaction been added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
    
    val currentTimestamp by remember { mutableLongStateOf(System.currentTimeMillis()) }


    var expanded by remember { mutableStateOf(false) }
    val showDateDialog = rememberSaveable { mutableStateOf(false) }
    val showTimeDialog = rememberSaveable { mutableStateOf(false) }


    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentTimestamp,
        initialDisplayMode = DisplayMode.Picker
    )

    val timePickerState = rememberTimePickerState(
        initialHour = currentTimestamp.toDateTime().hour,
        initialMinute = currentTimestamp.toDateTime().minute,
        is24Hour = false
    )

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onNavigateBack() }, modifier = Modifier.size(44.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp, top = 24.dp, bottom = 24.dp)
    ) { contentPadding ->
        if (showDateDialog.value) {
            DatePickerDialog(
                onDismissRequest = { showDateDialog.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onEventChanged(
                                ExpenseFormInputEvent.onDateChanged(it)
                            )
                        }
                        showDateDialog.value = false
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDateDialog.value = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }


        if (showTimeDialog.value) {
            TimePickerDialog(
                onConfirm = {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    cal.set(Calendar.MINUTE, timePickerState.minute)
                    cal.isLenient = false
                    onEventChanged(ExpenseFormInputEvent.onTimeChanged(cal.timeInMillis))
                    showTimeDialog.value = false
                },
                onCancel = { showTimeDialog.value = false },
            ) {
                TimePicker(state = timePickerState)
            }
        }

        Column(
            modifier = Modifier
                .padding(contentPadding)
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.merchant))
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = formState.merchantName,
                    onValueChange = { t -> onEventChanged(ExpenseFormInputEvent.onMerchantChanged(t)) },
                    isError = formState.merchantErrorMsg != null,
                    supportingText = {
                        if (formState.merchantErrorMsg != null) {
                            ErrorMessage(formState.merchantErrorMsg)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(stringResource(R.string.address))
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = formState.address,
                    onValueChange = { t ->
                        onEventChanged(
                            ExpenseFormInputEvent.onMerchantAddressChanged(t)
                        )
                    },
                    isError = formState.addressErrorMsg != null,
                    supportingText = {
                        if (formState.addressErrorMsg != null) {
                            ErrorMessage(formState.addressErrorMsg)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1.0f, true)
                    ) {
                        Text(stringResource(R.string.date))
                        Spacer(modifier = Modifier.height(4.dp))
                        TextField(
                            value = formState.date.toDateString("MMM d, yyyy"),
                            readOnly = true,
                            enabled = false,
                            onValueChange = {},
                            isError = formState.dateErrorMsg != null,
                            supportingText = {
                                             if (formState.dateErrorMsg != null) {
                                                 ErrorMessage(formState.dateErrorMsg)
                                             }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showDateDialog.value = true
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1.0f, true)
                    ) {
                        Text(stringResource(R.string.time))
                        Spacer(modifier = Modifier.height(4.dp))
                        TextField(
                            value = formState.time.toDateString("HH:mm a"),
                            readOnly = true,
                            enabled = false,
                            onValueChange = {},
                            isError = formState.timeErrorMsg != null,
                            supportingText = {
                                if (formState.timeErrorMsg != null) {
                                    ErrorMessage(formState.timeErrorMsg)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showTimeDialog.value = true
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

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
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        isError = formState.categoryErrorMsg != null,
                        supportingText = {
                            if (formState.categoryErrorMsg != null) {
                                ErrorMessage(formState.categoryErrorMsg)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
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
                                        ExpenseFormInputEvent.onCategoryChanged(
                                            categoryName
                                        )
                                    )
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text(stringResource(R.string.total))
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = formState.totalAmount,
                    onValueChange = { onEventChanged(ExpenseFormInputEvent.onTotalExpenseChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = formState.totalExpenseErrorMsg != null,
                    supportingText = {
                        if (formState.totalExpenseErrorMsg != null) {
                            ErrorMessage(formState.totalExpenseErrorMsg)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .height(44.dp), onClick = { onEventChanged(ExpenseFormInputEvent.onSubmit) }) {
                Text(text = stringResource(R.string.submit))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun ExpenseFormScreen() {
    PersonalFinanceTheme {
        Surface(modifier = Modifier.fillMaxSize(1.0f)) {
            ExpenseScreen(
                formState = TransactionForm(
                    date = System.currentTimeMillis(),
                    time = System.currentTimeMillis()
                ),
                validationEvents = Channel<ValidationEvent>().receiveAsFlow(),
                onNavigateBack = { /*TODO*/ },
                onEventChanged = { /*TODO*/ }
            )
        }
    }
}