package com.shayan.android101

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.shayan.android101.ui.screens.ProductScreen
import com.shayan.android101.ui.theme.Android101Theme
import com.shayan.android101.viewmodels.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            Android101Theme {
                ProductScreen(productViewModel)
            }
        }
    }
}
