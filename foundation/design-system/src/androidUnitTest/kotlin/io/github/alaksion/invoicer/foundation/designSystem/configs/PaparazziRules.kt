package io.github.alaksion.invoicer.foundation.designSystem.configs

import androidx.compose.runtime.Composable
import app.cash.paparazzi.Paparazzi
import io.github.alaksion.invoicer.foundation.designSystem.theme.InvoicerTheme

val InvoicerPaparazziConfig = Paparazzi(
    maxPercentDifference = 0.0
)

fun Paparazzi.invoicerSnapshot(
    name: String? = null,
    content: @Composable () -> Unit,
) {
    snapshot(name = name) {
        InvoicerTheme {
            content()
        }
    }
}
