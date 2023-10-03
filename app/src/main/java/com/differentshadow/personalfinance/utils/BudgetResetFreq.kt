package com.differentshadow.personalfinance.utils

enum class BudgetResetFreq(val title: String) {
    EveryDay("Daily"),
    EveryWeek("Weekly"),
    EveryMonth("Monthly"),
}

val BudgetResetFreqs = BudgetResetFreq.values().map {
    it.title
}