package com.differentshadow.personalfinance.ui.customanimation.utils

fun lerp(start: Float, stop: Float, fraction: Float) =
    (start * (1 - fraction) + stop * fraction)