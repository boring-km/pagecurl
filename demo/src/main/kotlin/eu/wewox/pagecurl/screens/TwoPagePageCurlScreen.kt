@file:OptIn(ExperimentalPageCurlApi::class)
@file:Suppress("MagicNumber")

package eu.wewox.pagecurl.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import eu.wewox.pagecurl.ExperimentalPageCurlApi
import eu.wewox.pagecurl.HowToPageData
import eu.wewox.pagecurl.config.PageCurlConfig
import eu.wewox.pagecurl.config.rememberPageCurlConfig
import eu.wewox.pagecurl.page.NextBackPageCurl
import eu.wewox.pagecurl.ui.SpacingLarge
import eu.wewox.pagecurl.ui.SpacingMedium

/**
 * Two-Page Configuration in Page Curl.
 * This screen demonstrates how to use the PageCurl library to create a two-page curl effect.
 */
@Composable
fun TwoPagePageCurlScreen() {
    Box(Modifier.fillMaxSize()) {
        val pages = remember { HowToPageData.twoPageCurlHowToPages }
        NextBackPageCurl(
            count = pages.size,
            config = rememberPageCurlConfig(
                dragInteraction = PageCurlConfig.StartEndDragInteraction(
                    PageCurlConfig.DragInteraction.PointerBehavior.PageEdge
                )
            ),
        ) { index ->
            ShowTwoPage(index, pages[index])
        }
    }
}

@Composable
fun ShowTwoPage(
    index: Int,
    page: HowToPageData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacingLarge),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = page.message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = index.toString(),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(topStartPercent = 100))
                .padding(SpacingMedium)
        )
    }
}
