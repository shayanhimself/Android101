package com.shayan.android101.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shayan.android101.datamodel.Product
import com.shayan.android101.ui.components.MyTopAppBar
import com.shayan.android101.ui.theme.Android101Theme
import com.shayan.android101.ui.theme.SpacingM
import com.shayan.android101.ui.theme.SpacingXL
import com.shayan.android101.ui.theme.SpacingXXS
import com.shayan.android101.viewmodels.ProductViewModel

@Composable
fun ProductScreen(
    viewModel: ProductViewModel
) {
    val product by viewModel.product.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MyTopAppBar() }
    ) { innerPadding ->
        product?.let {
            ProductContent(it, innerPadding)
        }
    }
}

@Composable
private fun ProductContent(
    product: Product,
    innerPadding: PaddingValues,
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Column {
            AsyncImage(
                model = product.image,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(SpacingM))

            Text(
                text = product.title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(SpacingM)
            )

            Spacer(modifier = Modifier.height(SpacingM))

            Text(
                text = product.description,
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductScreenDarkPreview() {
//    Android101Theme {
//        ProductScreen()
//    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ProductScreenLightPreview() {
//    Android101Theme {
//        ProductScreen()
//    }
}
