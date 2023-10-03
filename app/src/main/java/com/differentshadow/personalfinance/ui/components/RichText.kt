package com.differentshadow.personalfinance.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp

/**
 *
 * @param fullText The full text content to display with placeholders for links.
 *                  Example: "Visit Google to search"
 * @param linkText A map of link text and their corresponding URLs.
 *                 Example: mapOf("Google" to "https://www.google.com")
 */
@Composable
fun RichText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkTextColor: Color = MaterialTheme.colorScheme.primary,
    linkTextFontWeight: FontWeight = FontWeight.Normal,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    linkText: Map<String, String> = mapOf(),
    textStyle: TextStyle = LocalTextStyle.current,
    fontSize: TextUnit = TextUnit.Unspecified,
) {

    // UriHandler parse and opens URI inside AnnotatedString Item in Browse
    val uriHandler = LocalUriHandler.current

    val annotatedLinkString = buildAnnotatedString {
        append(fullText)
        if (linkText.isNotEmpty()) {
            linkText.forEach{ (text, url) ->
                val startIndex = fullText.indexOf(text)
                val endIndex = startIndex + text.length
                addStyle(
                    style = SpanStyle(
                        color = linkTextColor,
                        fontSize = if (fontSize.isUnspecified) textStyle.fontSize else fontSize,
                        textDecoration = linkTextDecoration,
                        fontWeight = linkTextFontWeight,
                        fontFamily = textStyle.fontFamily
                    ),
                    start = startIndex,
                    end = endIndex
                )
                addStringAnnotation(
                    tag = "URL",
                    annotation = url,
                    start = startIndex,
                    end = endIndex
                )
            }
        }

        addStyle(
            style = SpanStyle(
                fontSize = if (fontSize.isUnspecified) textStyle.fontSize else fontSize,
                fontFamily = textStyle.fontFamily
            ),
            start = 0,
            end = fullText.length
        )
    }

    ClickableText(modifier = modifier, text = annotatedLinkString, onClick = {
        if (linkText.isNotEmpty()) {
            annotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    })
}