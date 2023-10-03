package com.differentshadow.personalfinance.ui.screens

import android.graphics.PointF
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.model.TransactionUIState
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.ui.theme.PoppinsFamily
import kotlin.math.round

private fun translate(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    left: Float,
    top: Float = 0f,
    maxX: Float = 110f,
    maxY: Float = 34f
): PointF {
    return PointF(
        ((x / maxX) * width) + left,
        ((y / maxY) * height) + top
    )
}


private fun inverseTranslate(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    left: Float,
    top: Float = 0f,
    maxX: Float = 110f,
    maxY: Float = 34f
): PointF {
    return PointF(
        left - ((x / maxX) * width),
        top - ((y / maxY) * height)
    )
}

private fun createPath(path: Path, left: Float, width: Float = 50f, height: Float = 24f): Path {
    val endX = 130f
    path.cubicTo(
        translate(x = 24f, y = 0f, width = width, height = height, left = left, maxX = 110f).x,
        translate(x = 24f, y = 0f, width = width, height = height, left = left, maxX = 110f).y,
        translate(x = 26f, y = 28f, width = width, height = height, left = left, maxX = 110f).x,
        translate(x = 26f, y = 28f, width = width, height = height, left = left, maxX = 110f).y,

        translate(x = 65f, y = 28f, width = width, height = height, left = left, maxX = 110f).x,
        translate(x = 65f, y = 28f, width = width, height = height, left = left, maxX = 110f).y,
    )
    path.cubicTo(
        translate(x = 106f, y = 28f, width = width, height = height, left = left, maxX = 110f).x,
        translate(x = 106f, y = 28f, width = width, height = height, left = left, maxX = 110f).y,
        translate(x = 109f, y = 0f, width = width, height = height, left = left, maxX = 110f).x,
        translate(x = 109f, y = 0f, width = width, height = height, left = left, maxX = 110f).y,

        translate(x = endX, y = 0f, width = width, height = height, left = left, maxX = 110f).x,
        translate(x = endX, y = 0f, width = width, height = height, left = left, maxX = 110f).y,
    )
    path.lineTo(round(((endX / 110f) * width) + left), 0f)
    path.lineTo(round(((endX / 110f) * width) + left) + 24f, 0f)
    return path
}


private fun inverseCreatePath(
    path: Path,
    left: Float,
    width: Float = 50f,
    height: Float = 24f,
    startX: Float = 0f,
    startY: Float = 0f
): Path {
    val endX = 130f
    path.cubicTo(
        inverseTranslate(
            x = 24f,
            y = 0f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).x,
        inverseTranslate(
            x = 24f,
            y = 0f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).y,
        inverseTranslate(
            x = 26f,
            y = 28f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).x,
        inverseTranslate(
            x = 26f,
            y = 28f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).y,

        inverseTranslate(
            x = 65f,
            y = 28f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).x,
        inverseTranslate(
            x = 65f,
            y = 28f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).y,
    )
    path.cubicTo(
        inverseTranslate(
            x = 106f,
            y = 28f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).x,
        inverseTranslate(
            x = 106f,
            y = 28f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).y,
        inverseTranslate(
            x = 109f,
            y = 0f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).x,
        inverseTranslate(
            x = 109f,
            y = 0f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).y,

        inverseTranslate(
            x = endX,
            y = 0f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).x,
        inverseTranslate(
            x = endX,
            y = 0f,
            width = width,
            height = height,
            left = left,
            maxX = 110f,
            top = startY
        ).y,
    )
    path.lineTo(round(left - ((endX / 110f) * width)), startY)
    path.lineTo(round(left - ((endX / 110f) * width)) - 24f, startY)
    return path
}

@Composable
fun TransactionScreen(
    uiState: TransactionUIState,
    popUp: () -> Unit
) {

    val configuration = LocalConfiguration.current;
    val screenWidth = configuration.screenWidthDp - 64
    val transactionHeight = configuration.screenHeightDp * 0.64f

    val ticketColor = MaterialTheme.colorScheme.surface

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding(),
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp, top = 24.dp, bottom = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                IconButton(onClick = { popUp() }) {
                    Icon(painter = painterResource(id = R.drawable.chevron_left), contentDescription = "Navigate back")
                }
            }
            Card(
                modifier = Modifier
                    .width(screenWidth.dp)
                    .height(transactionHeight.dp)
                    .drawWithCache {
                        onDrawBehind {
                            val path = Path()
                            val range = ((size.width / 74f) - 2).toInt()


                            path.moveTo(0f, 0f)
                            path.lineTo(34f, 0f)
                            var left = 34f
                            for (i in 1..range) {
                                createPath(path = path, left = left)
                                left += 59 + 24
                            }

                            path.lineTo(size.width, 0f)
                            path.lineTo(size.width, size.height)
                            path.lineTo(size.width - 40f, size.height)

                            var right = size.width - 40f
                            for (i in 1..range) {
                                inverseCreatePath(
                                    path = path,
                                    left = kotlin.math.abs(right),
                                    startY = size.height,
                                    startX = size.width
                                )
                                right -= 59 + 24
                            }

                            path.lineTo(0f, size.height)
                            path.close()

                            drawPath(path, color = ticketColor)
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                colors = CardDefaults.elevatedCardColors()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.weight(1.0f, true),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Walmart",
                            fontSize = 24.sp,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "5940 LOSEE RD\nNORTH LAS VEGAS NV 89081",
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1.0f, true),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.transaction_no))
                            Text(text = "352")
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.date))
                            Text(text = "Aug 12, 2021")
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.time))
                            Text(text = "07:08 AM")
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.category))
                            Text(text = "Grocery")
                        }
                    }


                    Column(
                        modifier = Modifier.weight(1.0f, true),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 32.sp,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(text = "$35.36")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.0f, true),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.weight(1.0f, true)
                        ) {
                            Text("Delete")
                        }

                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1.0f, true)) {
                            Text("Edit")
                        }
                    }
                }
            }

        }
    }
}


@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun TransactionPreview() {
    PersonalFinanceTheme {
        Surface {
            TransactionScreen(uiState = TransactionUIState(),popUp = { /* TODO */})
        }
    }
}