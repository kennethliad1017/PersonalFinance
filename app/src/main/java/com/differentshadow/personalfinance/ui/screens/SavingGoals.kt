@file:OptIn(ExperimentalMaterial3Api::class)

package com.differentshadow.personalfinance.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.form.GoalFormInputEvent
import com.differentshadow.personalfinance.domain.form.SavingGoalForm
import com.differentshadow.personalfinance.domain.form.ValidationEvent
import com.differentshadow.personalfinance.domain.model.SavingGoalEntity
import com.differentshadow.personalfinance.domain.model.SavingGoalUIState
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.ui.theme.PoppinsFamily
import com.differentshadow.personalfinance.utils.CategoryList
import com.differentshadow.personalfinance.utils.toCurrency
import com.differentshadow.personalfinance.utils.toDateString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


@Composable
fun SavingCard(
    modifier: Modifier = Modifier,
    title: String,
    currentSaving: Double,
    goalSaving: Double,
    titleFontSize: TextUnit = TextUnit.Unspecified,
    titleFontFamily: FontFamily = PoppinsFamily,
    currentSavingFontSize: TextUnit = TextUnit.Unspecified,
    currentSavingFontFamily: FontFamily = PoppinsFamily,
    goalSavingFontSize: TextUnit = TextUnit.Unspecified,
    goalSavingFontFamily: FontFamily = PoppinsFamily
) {


    Column(modifier = modifier) {
//        TODO: Category Illustration Need to be added
        Text(
            text = title,
            fontSize = titleFontSize,
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.weight(1.0f, true), verticalAlignment = Alignment.Bottom) {
            Text(
                text = "${currentSaving.toCurrency()}",
                fontSize = currentSavingFontSize,
                fontFamily = currentSavingFontFamily,
                fontWeight = FontWeight.Bold,
                lineHeight = currentSavingFontSize.times(1.1f)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "/ ${goalSaving.toCurrency()}",
                fontSize = goalSavingFontSize,
                fontFamily = goalSavingFontFamily,
                fontWeight = FontWeight.Medium,
                lineHeight = goalSavingFontSize.times(1.1f)
            )
        }

        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = (currentSaving / goalSaving).toFloat(), modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(16.dp)),
            trackColor = MaterialTheme.colorScheme.surfaceColorAtElevation(24.dp)
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun TopBar(onNavigateBack: () -> Unit, onClickAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(96.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onNavigateBack() }, modifier = Modifier.size(44.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.chevron_left),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1.0f, true)) {
            Text(
                stringResource(R.string.saving_goals),
                fontSize = 24.sp,
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 26.sp
            )
            Text(
                stringResource(R.string.saving_desc),
                fontSize = 14.sp,
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
                onClick = { onClickAdd() }, modifier = Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingGoalScreen(
    savingGoalUiState: SavingGoalUIState,
    formState: SavingGoalForm,
    validationEvents: Flow<ValidationEvent>,
    onInputChanged: (event: GoalFormInputEvent) -> Unit,
    onItemNavigate: (dest: String) -> Unit,
    onNavigateBack: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp - 32


    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Goal has been added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }


    var expanded by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val showDialog = rememberSaveable { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = formState.goalDate,
        initialDisplayMode = DisplayMode.Picker
    )
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding(),
        topBar = {
            TopBar(
                onNavigateBack = onNavigateBack,
                onClickAdd = {
                    scope.launch {
                        if (!sheetState.isVisible) {
                            showBottomSheet = true
                        }
                    }
                })
        }
    ) { contentPadding ->
        if (showDialog.value) {
            DatePickerDialog(
                onDismissRequest = { showDialog.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { onInputChanged(GoalFormInputEvent.onGoalDateChanged(it)) }
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
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(stringResource(R.string.title))
                    Spacer(modifier = Modifier.height(4.dp))
                    TextField(
                        value = formState.title,
                        onValueChange = { onInputChanged(GoalFormInputEvent.onTitleChanged(it)) },
                        isError = formState.titleErrorMsg != null,
                        supportingText = {
                            if (formState.titleErrorMsg != null) {
                                Text(text = formState.titleErrorMsg)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

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
                            isError = formState.categoryErrorMsg != null,
                            supportingText = {
                                if (formState.categoryErrorMsg != null) {
                                    Text(text = formState.categoryErrorMsg)
                                }
                            },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
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
                                        onInputChanged(GoalFormInputEvent.onCategoryChanged(categoryName))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(stringResource(R.string.goal_amount))
                    Spacer(modifier = Modifier.height(4.dp))
                    TextField(
                        value = formState.goalAmount,
                        onValueChange = { onInputChanged(GoalFormInputEvent.onGoalAmountChanged(it)) },
                        isError = formState.goalAmountErrorMsg  != null,
                        supportingText = {
                            if (formState.goalAmountErrorMsg != null) {
                                Text(text = formState.goalAmountErrorMsg)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(stringResource(R.string.goal_date))
                    Spacer(modifier = Modifier.height(4.dp))
                    TextField(
                        value = formState.goalDate.toDateString("MMM d, yyyy"),
                        onValueChange = {},
                        isError = formState.goalDate != null,
                        supportingText = {
                                         if (formState.goalDateErrorMsg != null) {
                                             Text(text = formState.goalDateErrorMsg)
                                         }
                        },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDialog.value = true
                            },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )

                }

                Spacer(Modifier.height(12.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        onInputChanged(GoalFormInputEvent.onSubmit)
                    }
                ) {
                    Text(text = stringResource(R.string.submit))
                }

                Spacer(Modifier.height(24.dp))
            }
        }

        if (savingGoalUiState.savingGoals.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(1.0f)
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(screenWidth.dp),
                    painter = painterResource(id = R.drawable.savings_bro),
                    contentDescription = "Saving Goal"
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.saving_empty_desc),
                    fontFamily = PoppinsFamily,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(1.0f)
                    .padding(contentPadding)
            ) {
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    items(savingGoalUiState.savingGoals, key = { it.id }) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Card(
                            colors = CardDefaults.elevatedCardColors(),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            SavingCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(112.dp)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                title = it.title,
                                currentSaving = it.currentSaving,
                                currentSavingFontSize = 20.sp,
                                goalSaving = it.goalSaving,
                                goalSavingFontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun SavingGoalPreview() {
    val savingGoal1 = SavingGoalEntity(
        id = 1,
        title = "Vacation Fund",
        category = "Travel",
        currentSaving = 500.0,
        goalSaving = 2000.0,
        goalDate = "31, Dec 2023",
        isDeleted = false,
        createdAt = "Sat 17, Sep 2023"
    )

    val savingGoal2 = SavingGoalEntity(
        id = 2,
        title = "New Car",
        category = "Automobile",
        currentSaving = 1000.0,
        goalSaving = 15000.0,
        goalDate = "30, Jun 2024",
        isDeleted = false,
        createdAt = "Sun 18, Sep 2023"
    )

    val savingGoal3 = SavingGoalEntity(
        id = 3,
        title = "Emergency Fund",
        category = "Savings",
        currentSaving = 3000.0,
        goalSaving = 5000.0,
        goalDate = "31, Oct 2023",
        isDeleted = false,
        createdAt = "Fri 16, Sep 2023"
    )

    val savingGoalsList = listOf(savingGoal1, savingGoal2, savingGoal3)
    PersonalFinanceTheme {
        Surface(modifier = Modifier.fillMaxSize(1.0f)) {
            SavingGoalScreen(
                savingGoalUiState = SavingGoalUIState(
                    savingGoals = savingGoalsList
                ),
                formState = SavingGoalForm(),
                validationEvents = Channel<ValidationEvent>().receiveAsFlow(),
                onItemNavigate = { /*TODO*/ },
                onNavigateBack = { /*TODO*/ },
                onInputChanged =  { /*TODO*/ },
            )
        }
    }
}