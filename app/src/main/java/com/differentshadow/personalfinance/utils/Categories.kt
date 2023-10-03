package com.differentshadow.personalfinance.utils

import androidx.compose.ui.graphics.Color

enum class Categories(val title: String, val color: Color) {
    Grocery("Grocery", Color(0xFF8BC34A)),
    Dining("Dining",Color(0xFF2196F3)),
    Transportation("Transportation",  Color(0xFF9C27B0)),
    Utility("Utility", Color(0xFF009688)),
    RentMortgage("Rent/Mortgage", Color(0xFF795548)),
    Entertainment("Entertainment", Color(0xFFE91E63)),
    Health("Health", Color(0xFFCDDC39)),
    Fitness("Fitness", Color(0xFFFF9800)),
    Clothing("Clothing", Color(0xFF3F51B5)),
    PersonalCare("Personal Care", Color(0xFFFFC107)),
    Education("Education", Color(0xFF00BCD4)),
    Travel("Travel", Color(0xFFFF5722)),
    GiftsandDonations("Gifts and Donations",  Color(0xFFF43F5E)),
    HomeMaintenance("Home Maintenance", Color(0xFF673AB7)),
    Insurance("Insurance", Color(0xFF607D8B)),
    SavingsandInvestments("Savings and Investments", Color(0xFF4CAF50)),
    Miscellaneous("Miscellaneous", Color(0xFF9E9E9E)),
}

val CategoryList = Categories.values().map {
    it.title
}