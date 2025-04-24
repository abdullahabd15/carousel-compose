package com.absolution.carousel_compose

import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.absolution.carousel_compose.extensions.carouselTransition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ShowCaseBanner(
    imageUrls: List<String?>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 32.dp),
    pageSpacing: Dp = 16.dp,
    enableCarouselTransition: Boolean = false,
    autoPlay: Boolean = false,
    autoPlayInterval: Long = 5000L
) {
    val pageCount = Int.MAX_VALUE
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    if (autoPlay) {
        val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
        if (isDragged.not()) {
            with(pagerState) {
                var currentPageKey by remember { mutableStateOf(0) }
                LaunchedEffect(key1 = currentPageKey) {
                    launch {
                        delay(timeMillis = autoPlayInterval)
                        val nextPage = (currentPage + 1).mod(pageCount)
                        animateScrollToPage(page = nextPage)
                        currentPageKey = nextPage
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalPager(
            state = pagerState,
            contentPadding = contentPadding,
            pageSpacing = pageSpacing,
        ) { index ->
            val actualIndex = index % imageUrls.size
            val imageModifier = Modifier
                .then(
                    if (enableCarouselTransition)
                        Modifier.carouselTransition(page = index, pagerState = pagerState)
                    else
                        Modifier
                )
                .clip(RoundedCornerShape(12.dp))
            AsyncImage(
                modifier = imageModifier,
                model = imageUrls[actualIndex],
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        DotsIndicator(
            modifier = Modifier.fillMaxWidth(),
            itemCount = imageUrls.size,
            selectedItemIndex = pagerState.currentPage % imageUrls.size,
            arrangement = Arrangement.Center
        )
    }
}