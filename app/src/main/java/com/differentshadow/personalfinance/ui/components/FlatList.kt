package com.differentshadow.personalfinance.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FlatList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontal: Boolean = false,
    content: LazyListScope.() -> Unit
) {
    if (horizontal) {
        Column(
            modifier = modifier,
        ) {
//            Text(text = headerText, headerTextModifier)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = contentPadding,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        content = content
    )
}