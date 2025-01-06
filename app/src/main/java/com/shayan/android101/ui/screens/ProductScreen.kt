package com.shayan.android101.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shayan.android101.ui.theme.Android101Theme

@Composable
fun ProductScreen() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column {
            Text(
                text = "This Jacket That Makes You Cooler Than the Weather",
                color = MaterialTheme.colorScheme.primary,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Step into ultimate style and comfort with this versatile jacket. Whether you're braving chilly winds or just pretending it’s cold enough to look this good, this jacket's got you covered—literally. Bonus: It has pockets. Yes, REAL ones. \uD83D\uDD25",
                color = MaterialTheme.colorScheme.onBackground,
                style = TextStyle(
                    fontSize = 18.sp,
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Text(
            text = "$ 9.90",
            color = MaterialTheme.colorScheme.secondary,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 32.dp)
        )

        Text(
            text = "4.5 (340 ratings)",
            color = MaterialTheme.colorScheme.onBackground,
            style = TextStyle(
                fontSize = 18.sp,
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 32.dp)
        )
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
