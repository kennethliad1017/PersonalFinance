package com.differentshadow.personalfinance.domain.form

sealed class BudgetFormInputEvent {
    data class onCategoryChanged(val category: String): BudgetFormInputEvent()
    data class onBudgetLimitChanged(val budgetLimit: String): BudgetFormInputEvent()
    data class onResetFrequentChanged(val resetPeriod: String): BudgetFormInputEvent()
    data class onExpenseChanged(val expense: String): BudgetFormInputEvent()

    object onSubmit: BudgetFormInputEvent()
    object onUpdate: BudgetFormInputEvent()
}
