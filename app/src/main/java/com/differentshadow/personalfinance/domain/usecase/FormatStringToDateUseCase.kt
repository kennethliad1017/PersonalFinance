package com.differentshadow.personalfinance.domain.usecase

import java.text.SimpleDateFormat
import javax.inject.Inject

class FormatStringToDateUseCase @Inject constructor() {

    operator fun invoke(date: String, pattern: String): Long {
        val formatter = SimpleDateFormat(pattern)
        return formatter.parse(date).time
    }
}