package com.differentshadow.personalfinance.domain.form

sealed class ValidationEvent {
    object Success: ValidationEvent()
}
