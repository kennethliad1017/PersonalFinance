package com.differentshadow.personalfinance.domain.form

sealed class GoalFormInputEvent {
    data class onTitleChanged(val title: String): GoalFormInputEvent()
    data class onCategoryChanged(val category: String): GoalFormInputEvent()
    data class onGoalAmountChanged(val goalAmount: String): GoalFormInputEvent()
    data class onGoalDateChanged(val date: Long): GoalFormInputEvent()

    object onSubmit: GoalFormInputEvent()
}
