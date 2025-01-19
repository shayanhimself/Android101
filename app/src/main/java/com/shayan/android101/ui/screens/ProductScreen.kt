package com.shayan.android101.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shayan.android101.R
import com.shayan.android101.datamodel.Product
import com.shayan.android101.datamodel.Rating
import com.shayan.android101.ui.components.MyTopAppBar
import com.shayan.android101.ui.theme.Android101Theme
import com.shayan.android101.ui.theme.SpacingL
import com.shayan.android101.ui.theme.SpacingM
import com.shayan.android101.ui.theme.SpacingXL
import com.shayan.android101.ui.theme.SpacingXXS
import com.shayan.android101.viewmodels.ProductViewModel

@Composable
fun ProductScreen(
    viewModel: ProductViewModel,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    ProductScreen(
        viewState = viewState,
        onRefresh = viewModel::refresh
    )
}

@Composable
private fun ProductScreen(
    viewState: ProductViewModel.ViewState,
    onRefresh: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MyTopAppBar() }
    ) { innerPadding ->

        viewState.product?.let {
            ProductContent(it, innerPadding)
        }

        if (viewState.isLoading) {
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = innerPadding.calculateTopPadding() + SpacingXXS)
            )
        }

        if (viewState.hasError) {
            ErrorMessage(innerPadding, onRefresh)
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
            text = stringResource(R.string.price_formatted, product.price),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = SpacingM, bottom = SpacingXL)
        )

        val rate = product.rating.rate
        val count = product.rating.count

        Text(
            text = stringResource(R.string.rating_formatted, rate, count),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = SpacingM, bottom = SpacingXL)
        )
    }
}

@Composable
private fun ErrorMessage(
    innerPadding: PaddingValues,
    onRefresh: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.error_general),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(SpacingL))

        Button(onClick = onRefresh) {
            Text(
                text = stringResource(R.string.retry),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = SpacingM),
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductScreenDarkPreview() {
    Android101Theme {
        ProductScreen(
            viewState = ProductViewModel.ViewState(
                product = Product(
                    id = 1,
                    title = "T-Shirt of Infinite Comfort and Questionable Decisions",
                    description = "This t-shirt doesn't just sit on your torso—it defines your vibe. Perfect for lazy Sundays, or making that one friend ask, \"What's going on?\".\nSoft, breathable, and questionable in all the right ways. Warning: May attract compliments. \uD83E\uDD9D\uD83D\uDD25",
                    image = "https://fakestoreapi.shayanaryan.com/img/51eg55uWmdL.jpg",
                    category = "Men's clothing",
                    price = 19.90f,
                    rating = Rating(
                        rate = 4.9f,
                        count = 238
                    )
                ),
                isLoading = false,
            ),
            onRefresh = {},
        )
    }
}

@Preview
@Composable
fun ProductScreenLoadingPreview() {
    Android101Theme {
        ProductScreen(
            viewState = ProductViewModel.ViewState(
                product = null,
                isLoading = true,
            ),
            onRefresh = {},
        )
    }
}

@Preview
@Composable
fun ProductScreenErrorPreview() {
    Android101Theme {
        ProductScreen(
            viewState = ProductViewModel.ViewState(
                product = null,
                isLoading = false,
                hasError = true,
            ),
            onRefresh = {},
        )
    }
}
