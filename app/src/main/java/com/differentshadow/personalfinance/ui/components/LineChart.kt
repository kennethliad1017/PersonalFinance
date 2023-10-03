package com.differentshadow.personalfinance.ui.components

import android.graphics.PointF
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


val PADDING = 16

data class DataPoint (
    val date: Int,
    val value: Double,
)

val dummyData = listOf(
    DataPoint(date = 1599119580, value = 10365.763144),
    DataPoint(date = 1599119520, value = 10354.38030756),
    DataPoint(date = 1599119460, value = 10349.18557836),
    DataPoint(date = 1599119400, value = 10346.1234222),
    DataPoint(date = 1599119340, value = 10346.88896124),
    DataPoint(date = 1599119280, value = 10344.2551424),
    DataPoint(date = 1599119220, value = 10348.25599524),
    DataPoint(date = 1599119160, value = 10348.34713084),
    DataPoint(date = 1599119100, value = 10348.28333592),
    DataPoint(date = 1599119040, value = 10347.37197992),
    DataPoint(date = 1599118980, value = 10347.01655108),
    DataPoint(date = 1599118920, value = 10340.39099296),
    DataPoint(date = 1599118860, value = 10340.9742608),
    DataPoint(date = 1599118800, value = 10332.64446696),
    DataPoint(date = 1599118740, value = 10334.5036332),
    DataPoint(date = 1599118680, value = 10332.64446696),
    DataPoint(date = 1599118620, value = 10340.58237772),
    DataPoint(date = 1599118560, value = 10340.58237772),
    DataPoint(date = 1599118500, value = 10340.58237772),
    DataPoint(date = 1599118440, value = 10340.5641506),
    DataPoint(date = 1599118380, value = 10338.95105048),
    DataPoint(date = 1599118320, value = 10340.29985736),
    DataPoint(date = 1599118260, value = 10337.65692496),
    DataPoint(date = 1599118200, value = 10336.39014012),
    DataPoint(date = 1599118140, value = 10335.70662312),
    DataPoint(date = 1599118080, value = 10329.8557176),
    DataPoint(date = 1599118020, value = 10328.33375308),
    DataPoint(date = 1599117960, value = 10326.36522412),
    DataPoint(date = 1599117900, value = 10325.49032236),
    DataPoint(date = 1599117840, value = 10316.62282848),
    DataPoint(date = 1599117780, value = 10306.55234468),
    DataPoint(date = 1599117720, value = 10310.82660432),
    DataPoint(date = 1599117660, value = 10310.7628094),
    DataPoint(date = 1599117600, value = 10303.6542326),
    DataPoint(date = 1599117540, value = 10303.6542326),
    DataPoint(date = 1599117480, value = 10306.59791248),
    DataPoint(date = 1599117420, value = 10308.20189904),
    DataPoint(date = 1599117360, value = 10314.79100292),
    DataPoint(date = 1599117300, value = 10308.81250756),
    DataPoint(date = 1599117240, value = 10300.04526284),
    DataPoint(date = 1599117180, value = 10304.1554784),
    DataPoint(date = 1599117120, value = 10291.95242156),
    DataPoint(date = 1599117060, value = 10288.44370096),
    DataPoint(date = 1599117000, value = 10267.336696),
    DataPoint(date = 1599116940, value = 10264.74844496),
    DataPoint(date = 1599116880, value = 10259.20740048),
    DataPoint(date = 1599116820, value = 10275.5844678),
    DataPoint(date = 1599116760, value = 10275.54801356),
    DataPoint(date = 1599116700, value = 10279.58532064),
    DataPoint(date = 1599116640, value = 10280.70628852),
    DataPoint(date = 1599116580, value = 10294.88698788),
    DataPoint(date = 1599116520, value = 10296.64590496),
    DataPoint(date = 1599116460, value = 10295.29709808),
    DataPoint(date = 1599116400, value = 10288.92671964),
    DataPoint(date = 1599116340, value = 10279.7767054),
    DataPoint(date = 1599116280, value = 10288.070045),
    DataPoint(date = 1599116220, value = 10301.44875108),
    DataPoint(date = 1599116160, value = 10264.74844496),
    DataPoint(date = 1599116100, value = 10272.79571844),
    DataPoint(date = 1599116040, value = 10277.1793408),
)

data class Point(
    val x: Float,
    val y: Float,
)

data class Vector(
    val x: Number,
    val y: Number
)

data class PolarCoordinates(val theta: Float, val radius: Float)

enum class Strategy {
    COMPLEX,
    BEZIER,
    SIMPLE
}

data class Graph (
    val label: String,
    val minPrice: Double,
    val maxPrice: Double,
    val path: Path,
)


fun buildGraph(dataPoints: List<DataPoint>, label: String, size: Size): Graph {
    val ADJUSTED_SIZE = size.height - PADDING * 2
    val priceList = dataPoints.reversed()

    val prices = priceList.map {item -> item.value }
    val dates = priceList.map { item -> item.date }

    val minDate = dates.minBy { it }
    val maxDate = dates.maxBy { it }
    val minPrice = prices.minBy { it }
    val maxPrice = prices.maxBy { it  }
    val points: List<Point> = priceList.map {
        val x = ((it.date - minDate) / (maxDate - minDate)) * size.width
        val y = ((it.value - minPrice) / (maxPrice - minPrice)) * ADJUSTED_SIZE
        Point(x = x, y = y.toFloat())
    }

    points.plus(Point(x = (size.width + 10), y = points[points.size - 1].y))
    val path = curveLines(points, 0.1f, Strategy.BEZIER)

    return Graph(label= label, minPrice = minPrice, maxPrice = maxPrice, path = path)
}

fun cartesian2Polar(v: Point): PolarCoordinates {
    val theta = atan2(v.x, v.y)
    val radius = sqrt(v.x.pow(2.0f) + v.y.pow(2.0f))

    return PolarCoordinates(theta = theta, radius = radius)
}

fun curveLines (points: List<Point>, smoothing: Float, strategy: Strategy): Path {

    val path = Path()

    // build the d attributes by looping over the points
    points.forEachIndexed { i, _ ->
        if (i == 0) {
            path.moveTo(points[i].x, points[i].y)
        }
        val point = points[i]
        val next = points.getOrNull(i + 1) ?: point
        val previous = points.getOrNull(i - 1) ?: point
        val cps = controlPoint(current = previous, previous = points.getOrNull(i - 2), next = point, reverse = false, smoothing = smoothing)
        val cpe = controlPoint(current = point, previous = previous, next = next, reverse = true, smoothing = smoothing)

        when (strategy) {
            Strategy.COMPLEX -> {
                path.cubicTo(cps.x, cps.y, cpe.x, cpe.y, point.x, point.y);
            }
            Strategy.BEZIER -> {

                val p0 = points.getOrNull(i - 2) ?: previous;
                val p1 = points.getOrNull(i - 1) ?: previous;
                val cp1x = (2 * p0.x + p1.x) / 3;
                val cp1y = (2 * p0.y + p1.y) / 3;
                val cp2x = (p0.x + 2 * p1.x) / 3;
                val cp2y = (p0.y + 2 * p1.y) / 3;
                val cp3x = (p0.x + 4 * p1.x + point.x) / 6;
                val cp3y = (p0.y + 4 * p1.y + point.y) / 6;


                path.cubicTo(cp1x, cp1y, cp2x, cp2y, cp3x, cp3y)
                if (i == points.size - 1) {
                    path.cubicTo(
                        points[points.size - 1].x,
                        points[points.size - 1].y,
                        points[points.size - 1].x,
                        points[points.size - 1].y,
                        points[points.size - 1].x,
                        points[points.size - 1].y
                    );
                }
            }
            Strategy.SIMPLE -> {
                val cp = Vector(x = (cps.x + cpe.x) / 2, y = (cps.y + cpe.y) / 2);
                path.quadraticBezierTo(cp.x.toFloat(), cp.y.toFloat(), point.x.toFloat(), point.y.toFloat());
            }
        }
    }
    return path

}


fun controlPoint(current: Point, previous: Point?, next: Point?, reverse: Boolean, smoothing: Float): Point {
    val p = previous ?: current
    val n = next ?: current
    // Properties of the opposed-line
    val lengthX = n.x - p.x;
    val lengthY = n.y - p.y;

    val o = cartesian2Polar(Point(x = lengthX, y = lengthY))
    // If is end-control-point, add PI to the angle to go backward
    val angle = o.theta.plus((if (reverse) Math.PI else 0).toFloat())
    val length = o.radius * smoothing
    // The control point position is relative to the current point
    val x = current.x + cos(angle) * length
    val y = current.y + sin(angle) * length

    return Point(x = x, y = y)
}