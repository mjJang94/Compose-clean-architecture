package com.mj.compose_clean_architecture.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mj.compose_clean_architecture.R
import com.mj.compose_clean_architecture.ui.theme.Typography

@Composable
fun NetworkError(
    modifier: Modifier = Modifier,
    onRetryButtonClick: () -> Unit,
) {
   Column(
       modifier = modifier.fillMaxSize(),
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally,
   ) {
       Text(
           text = stringResource(R.string.network_error_title),
           style = Typography.bodyMedium,
           textAlign = TextAlign.Center,
       )

       Text(
           text = stringResource(R.string.network_error_description),
           style = Typography.bodySmall,
           modifier = Modifier.padding(16.dp),
           textAlign = TextAlign.Center,
       )

       Button(onClick = { onRetryButtonClick() }) {
           Text(
               text = stringResource(R.string.network_error_retry_button_text).uppercase()
           )
       }
   }
}

@Preview(showBackground = true)
@Composable
fun NetworkErrorPreview() {
    NetworkError(modifier = Modifier, onRetryButtonClick = {})
}