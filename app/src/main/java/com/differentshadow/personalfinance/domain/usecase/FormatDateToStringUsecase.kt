package com.differentshadow.personalfinance.domain.usecase

import java.text.SimpleDateFormat
import javax.inject.Inject

class FormatDateToStringUseCase @Inject constructor() {

    operator fun invoke(date: Long, pattern: String): String {
        val formatter = SimpleDateFormat(pattern)
        return formatter.format(date)
    }
}