package com.differentshadow.personalfinance.ui.customanimation.shape


import android.graphics.PointF
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

class IndentPath(
    private val rect: Rect,
) {
    private val maxX = 110f
    private val maxY = 34f

    private fun translate(x: Float, y: Float): PointF {
        return PointF(
            ((x / maxX) * rect.width) + rect.left,
            ((y / maxY) * rect.height) + rect.top
        )
    }

    fun createPath(): Path {
        val start = translate(x = -10f, y = 0f)
        val middle = translate(x = 55f, y = 28f)
        val end = translate(x = 120f, y = 0f)

        val control1 = translate(x = 14f, y = 0f)
        val control2 = translate(x = 16f, y = 28f)
        val control3 = translate(x = 96f, y = 28f)
        val control4 = translate(x = 99f, y = 28f)

        val path = Path()
        path.moveTo(start.x, start.y)
        path.cubicTo(control1.x, control1.y, control2.x, control2.y, middle.x, middle.y)
        path.cubicTo(control3.x, control3.y, control4.x, control4.y, end.x, end.y)


        return path
    }
}