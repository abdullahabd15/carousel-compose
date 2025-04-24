package com.absolution.carousel_compose

import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FullWidthBanner(
    imageUrls: List<String?>,
    modifier: Modifier = Modifier,
    showIndicator: Boolean = true,
    autoPlay: Boolean = false,
    autoPlayInterval: Long = 5000L,
    indicatorPosition: IndicatorPosition = IndicatorPosition.OUTSIDE_BOTTOM,
    indicatorArrangement: Arrangement.Horizontal = Arrangement.Center,
) {
    val pagerState = rememberPagerState(initialPage = 0) { imageUrls.size }
    if (autoPlay) {
        val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
        if (isDragged.not()) {
            with(pagerState) {
                var currentPageKey by remember { mutableStateOf(0) }
                LaunchedEffect(key1 = currentPageKey) {
                    launch {
                        delay(timeMillis = autoPlayInterval)
                        val nextPage = (currentPage + 1).mod(imageUrls.size)
                        animateScrollToPage(page = nextPage)
                        currentPageKey = nextPage
                    }
                }
            }
        }
    }

    val pager = @Composable {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
        ) { index ->
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = imageUrls[index],
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
            )
        }
    }

    val indicator = @Composable {
        DotsIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (indicatorPosition == IndicatorPosition.INSIDE_BOTTOM) 8.dp else 0.dp)
                .padding(horizontal = 8.dp),
            itemCount = imageUrls.size,
            selectedItemIndex = pagerState.currentPage,
            arrangement = indicatorArrangement
        )
    }

    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        when (indicatorPosition) {
            IndicatorPosition.INSIDE_BOTTOM -> Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                pager()
                if (showIndicator) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ) {
                        indicator()
                    }
                }
            }
            IndicatorPosition.OUTSIDE_BOTTOM -> Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                pager()
                if (showIndicator) {
                    Spacer(modifier = Modifier.height(8.dp))
                    indicator()
                }
            }
        }
    }
}