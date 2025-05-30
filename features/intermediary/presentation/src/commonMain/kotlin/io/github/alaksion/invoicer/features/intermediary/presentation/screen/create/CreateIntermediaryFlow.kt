package io.github.alaksion.invoicer.features.intermediary.presentation.screen.create

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import io.github.alaksion.invoicer.features.intermediary.presentation.screen.create.steps.IntermediaryNameStep
import io.github.alaksion.invoicer.features.intermediary.presentation.screen.feedback.IntermediaryFeedbackScreen

internal class CreateIntermediaryFlow : Screen {

    @Composable
    override fun Content() {
        Navigator(
            onBackPressed = {
                it.key != IntermediaryFeedbackScreen.SCREEN_KEY
            },
            screen = IntermediaryNameStep()
        ) { SlideTransition(it) }
    }
}
