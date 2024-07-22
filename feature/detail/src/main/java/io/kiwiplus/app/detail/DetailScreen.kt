package io.kiwiplus.app.detail

import android.net.http.SslError
import android.util.Log
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mj.compose_clean_architecture.ui.base.SIDE_EFFECTS_KEY
import com.mj.compose_clean_architecture.ui.theme.ComposeCleanArchitectureTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    state: DetailContract.State,
    effectFlow: Flow<DetailContract.Effect>?,
    onEventSent: (event: DetailContract.Event) -> Unit,
    onNavigationRequested: (effect: DetailContract.Effect.Navigation) -> Unit,
) {

    var isLoading by remember { mutableStateOf(true) }
    val animatedProgress by animateFloatAsState(targetValue = state.progress.toFloat(), animationSpec = tween(durationMillis = 500), label = "")

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is DetailContract.Effect.Navigation.ToMain -> onNavigationRequested(effect)
            }
        }?.collect()
    }

    Column(modifier = modifier) {

        Toolbar(onClose = { onEventSent(DetailContract.Event.Back) })

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { animatedProgress / 100f },
            )
        }
        WebView(
            url = state.newsUrl,
            onLoadFinished = { isLoading = false },
            onProgressChanged = { progress -> onEventSent(DetailContract.Event.Loading(progress)) })
    }
}

@Composable
private fun Toolbar(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 15.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.clickable(onClick = onClose),
            imageVector = Icons.Outlined.Close,
            contentDescription = "close",
        )
    }
}

@Composable
private fun WebView(
    url: String,
    onLoadFinished: () -> Unit,
    onProgressChanged: (Int) -> Unit,
) {
    AndroidView(factory = {
        WebView(it).apply {
            this.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                    super.onReceivedSslError(view, handler, error)
                    handler.proceed()
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    onLoadFinished()
                }
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    view.loadUrl(request.url.toString())
                    return true
                }
            }
            this.webChromeClient = object : WebChromeClient() {

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    onProgressChanged(newProgress)
                }
            }
        }
    }, update = {
        it.loadUrl(url)
    })
}

@Preview
@Composable
private fun DetailScreenPreview() {
    ComposeCleanArchitectureTheme {
        DetailScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            state = DetailContract.State(newsUrl = "", progress = 0),
            effectFlow = flow {},
            onEventSent = {},
            onNavigationRequested = { }

        )
    }
}

@Preview
@Composable
private fun ToolbarPreview() {
    ComposeCleanArchitectureTheme {
        Toolbar(onClose = {})
    }
}