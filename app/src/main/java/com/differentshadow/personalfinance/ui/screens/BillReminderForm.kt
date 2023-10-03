package com.differentshadow.personalfinance.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.form.BillReminderForm
import com.differentshadow.personalfinance.domain.form.BillReminderInputEvent
import com.differentshadow.personalfinance.domain.form.ValidationEvent
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.utils.CategoryList
import com.differentshadow.personalfinance.utils.toDateString
import com.differentshadow.personalfinance.utils.toDateTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillReminderFormScreen(
    formState: BillReminderForm,
    validationEvents: Flow<ValidationEvent>,
    onNavigateBack: () -> Unit,
    onEventChanged: (event: BillReminderInputEvent) -> Unit,
){

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Bill Reminder been added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    var currentTimestamp by remember { mutableLongStateOf(System.currentTimeMillis()) }


    var expanded by remember { mutableStateOf(false) }
    var showDateDialog = rememberSaveable { mutableStateOf(false) }
    var showTimeDialog = rememberSaveable { mutableStateOf(false) }


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
        contentWindowInsets = WindowInsets(left = 16.dp, top = 24.dp, right = 16.dp, bottom = 24.dp)
    ) {
        if (showDateDialog.value) {
            DatePickerDialog(
                onDismissRequest = { showDateDialog.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onEventChanged(
                                BillReminderInputEvent.onDateChanged(it)
                            )
                        }
                        showDateDialog.value = false
                    }) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDateDialog.value = false }) {
                        Text("Cancel")
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
                    onEventChanged(BillReminderInputEvent.onTimeChanged(cal.timeInMillis))
                    showTimeDialog.value = false
                },
                onCancel = { showTimeDialog.value = false },
            ) {
                TimePicker(state = timePickerState)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .fillMaxWidth()
            ) {
                Text("Title")
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = formState.title,
                    onValueChange = { t -> onEventChanged(BillReminderInputEvent.onTitleChanged(t)) },
                    isError = formState.titleErrorMsg != null,
                    supportingText = {
                        if (formState.titleErrorMsg != null) {
                            ErrorMessage(formState.titleErrorMsg)
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
                        Text("Date")
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
                        Text("Time")
                        Spacer(modifier = Modifier.height(4.dp))
                        TextField(
                            value = formState.time.toDateString("hh:mm a"),
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
                                        BillReminderInputEvent.onCategoryChanged(
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

                Text(stringResource(R.string.total_amount))
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = formState.billAmount,
                    onValueChange = {t -> onEventChanged(BillReminderInputEvent.onBillAmount(t)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = formState.billAmountErrorMsg != null,
                    supportingText = {
                        if (formState.billAmountErrorMsg != null) {
                            ErrorMessage(formState.billAmountErrorMsg)
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
                .height(44.dp), onClick = { onEventChanged(BillReminderInputEvent.onSubmit) }) {
                Text(text = stringResource(R.string.submit))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun BillReminderFormPreview() {

    PersonalFinanceTheme {
        Surface(modifier = Modifier.fillMaxSize(1.0f)) {
            BillReminderFormScreen(
                formState = BillReminderForm(
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