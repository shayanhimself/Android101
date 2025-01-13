package com.shayan.android101.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.shayan.android101.R
import com.shayan.android101.ui.theme.BlueDark
import com.shayan.android101.ui.theme.Orange500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Orange500,
            titleContentColor = BlueDark
        )
    )
}