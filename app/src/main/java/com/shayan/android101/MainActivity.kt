package com.shayan.android101

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.shayan.android101.navigation.ProductDetailRoute
import com.shayan.android101.navigation.ProductListRoute
import com.shayan.android101.ui.screens.ProductListScreen
import com.shayan.android101.ui.screens.ProductScreen
import com.shayan.android101.ui.theme.Android101Theme
import com.shayan.android101.viewmodels.ProductListViewModel
import com.shayan.android101.viewmodels.ProductViewModel
import com.shayan.android101.viewmodels.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android101Theme {
                val backStack = remember { mutableStateListOf<Any>(ProductListRoute) }

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = entryProvider {
                        entry<ProductListRoute> {
                            val viewModel: ProductListViewModel = viewModel()
                            ProductListScreen(
                                viewModel = viewModel,
                                onProductClick = { productId ->
                                    backStack.add(ProductDetailRoute(productId))
                                }
                            )
                        }

                        entry<ProductDetailRoute> { route ->
                            val viewModel: ProductViewModel = viewModel(
                                factory = ProductViewModelFactory(route.productId)
                            )
                            ProductScreen(
                                viewModel = viewModel,
                                onBackClick = { backStack.removeLastOrNull() }
                            )
                        }
                    }
                )
            }
        }
    }
}
