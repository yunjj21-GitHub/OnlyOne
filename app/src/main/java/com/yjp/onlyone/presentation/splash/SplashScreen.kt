package com.yjp.onlyone.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorResource(R.color.white),
                        colorResource(R.color.primary_blue),
                    ),
                ),
            ),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_paw),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(160.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    OnlyOneTheme {
        SplashScreen()
    }
}
