package com.shayan.android101.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shayan.android101.ui.components.MyTopAppBar
import com.shayan.android101.ui.theme.Android101Theme
import com.shayan.android101.ui.theme.SpacingM
import com.shayan.android101.ui.theme.SpacingXL

@Composable
fun ProductScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MyTopAppBar() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                AsyncImage(
                    model = "https://fakestoreapi.shayanaryan.com/img/51eg55uWmdL.jpg",
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                Spacer(modifier = Modifier.height(SpacingM))

                Text(
                    text = "T-Shirt of Infinite Comfort and Questionable Decisions",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(SpacingM)
                )

                Spacer(modifier = Modifier.height(SpacingM))

                Text(
                    text = "This t-shirt doesn't just sit on your torso—it defines your vibe. Perfect for lazy Sundays, or making that one friend ask, \"What's going on?\".\nSoft, breathable, and questionable in all the right ways. Warning: May attract compliments. \uD83E\uDD9D\uD83D\uDD25",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = SpacingM)
                )
            }

            Text(
                text = "$ 19.99",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = SpacingM, bottom = SpacingXL)
            )

            Text(
                text = "4.5 (238 ratings)",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = SpacingM, bottom = SpacingXL)
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductScreenDarkPreview() {
    Android101Theme {
        ProductScreen()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ProductScreenLightPreview() {
    Android101Theme {
        ProductScreen()
    }
}
