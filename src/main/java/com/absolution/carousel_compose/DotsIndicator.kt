package com.absolution.carousel_compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

enum class IndicatorPosition {
    INSIDE_BOTTOM, OUTSIDE_BOTTOM
}

@Composable
fun DotsIndicator(
    itemCount: Int,
    selectedItemIndex: Int,
    modifier: Modifier = Modifier,
    dotsColor: Color = Color.LightGray,
    shape: RoundedCornerShape = CircleShape,
    dotSize: Int = 8,
    arrangement: Arrangement.Horizontal = Arrangement.Center
) {
    Row(
        modifier = modifier,
        horizontalArrangement = arrangement
    ) {
        repeat(itemCount) { index ->
            val size = if (selectedItemIndex == index)
                DpSize(width = (dotSize * 2).dp, height = dotSize.dp)
            else
                DpSize(width = dotSize.dp, height = dotSize.dp)
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .background(color = dotsColor, shape = shape)
                    .size(size = size)
            )
        }
    }
}