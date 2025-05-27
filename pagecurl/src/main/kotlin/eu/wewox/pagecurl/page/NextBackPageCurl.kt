package eu.wewox.pagecurl.page

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import eu.wewox.pagecurl.ExperimentalPageCurlApi
import eu.wewox.pagecurl.config.PageCurlConfig
import eu.wewox.pagecurl.config.rememberPageCurlConfig

/**
 * Shows the pages which may be turned by drag or tap gestures.
 * This composable is used to create a page curl effect with next and back pages.
 *
 * @param count The count of pages.
 * @param modifier The modifier for this composable.
 * @param state The state of the PageCurl. Use this to programmatically change the current page or observe changes.
 * @param config The configuration for PageCurl.
 * @param content The content lambda to provide the page composable. Receives the page number.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@ExperimentalPageCurlApi
@Composable
public fun NextBackPageCurl(
    count: Int,
    modifier: Modifier = Modifier,
    state: PageCurlState = rememberPageCurlState(),
    config: PageCurlConfig = rememberPageCurlConfig(),
    content: @Composable (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    BoxWithConstraints(modifier) {
        state.setup(count, constraints)

        val updatedCurrent by rememberUpdatedState(state.current)
        val internalState by rememberUpdatedState(state.internalState ?: return@BoxWithConstraints)

        val updatedConfig by rememberUpdatedState(config)

        val dragGestureModifier = when (val interaction = updatedConfig.dragInteraction) {
            is PageCurlConfig.GestureDragInteraction ->
                Modifier
                    .dragGesture(
                        dragInteraction = interaction,
                        state = internalState,
                        enabledForward = updatedConfig.dragForwardEnabled && updatedCurrent < state.max - 1,
                        enabledBackward = updatedConfig.dragBackwardEnabled && updatedCurrent > 0,
                        scope = scope,
                        onChange = { state.current = updatedCurrent + it }
                    )

            is PageCurlConfig.StartEndDragInteraction ->
                Modifier
                    .dragStartEnd(
                        dragInteraction = interaction,
                        state = internalState,
                        enabledForward = updatedConfig.dragForwardEnabled && updatedCurrent < state.max - 1,
                        enabledBackward = updatedConfig.dragBackwardEnabled && updatedCurrent > 0,
                        scope = scope,
                        onChange = { state.current = updatedCurrent + it }
                    )
        }

        Box(
            Modifier
                .then(dragGestureModifier)
                .tapGesture(
                    config = updatedConfig,
                    scope = scope,
                    onTapForward = state::next,
                    onTapBackward = state::prev,
                )
        ) {
            // Wrap in key to synchronize state updates
            key(updatedCurrent, internalState.forward.value, internalState.backward.value) {
                if (updatedCurrent + 1 < state.max) {
                    content(updatedCurrent + 1)
                }

                if (updatedCurrent < state.max) {
                    val forward = internalState.forward.value
                    Box(Modifier.drawFlatContent(forward.top, forward.bottom)) {
                        content(updatedCurrent)
                    }
                    Box(Modifier.drawBothSideCurl(updatedConfig, forward.top, forward.bottom)) {
                        if (updatedCurrent + 1 < state.max) {
                            content(updatedCurrent + 1)
                        }
                    }
                }

                if (updatedCurrent > 0) {
                    val backward = internalState.backward.value
                    Box(Modifier.drawFlatContent(backward.top, backward.bottom)) {
                        content(updatedCurrent - 1)
                    }
                    Box(Modifier.drawBothSideCurl(updatedConfig, backward.top, backward.bottom)) {
                        content(updatedCurrent)
                    }
                }
            }
        }
    }
}