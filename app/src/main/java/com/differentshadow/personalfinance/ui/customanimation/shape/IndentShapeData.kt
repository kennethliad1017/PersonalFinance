package com.differentshadow.personalfinance.ui.customanimation.shape

import com.differentshadow.personalfinance.ui.customanimation.animation.indentshape.*;

data class IndentShapeData(
    val xIndent: Float = 0f,
    val height: Float = 0f,
    val width: Float = 0f,
    val cornerRadius: ShapeCornerRadius = shapeCornerRadius(0f),
    val ballOffset: Float = 0f,
)
