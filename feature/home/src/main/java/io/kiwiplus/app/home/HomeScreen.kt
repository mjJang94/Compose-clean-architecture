package io.kiwiplus.app.home

import android.graphics.Typeface
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mj.compose_clean_architecture.common.ktx.applyDateFormat
import com.mj.compose_clean_architecture.model.NewsInfo.Content
import com.mj.compose_clean_architecture.ui.base.SIDE_EFFECTS_KEY
import com.mj.compose_clean_architecture.ui.theme.ComposeCleanArchitectureTheme
import com.mj.compose_clean_architecture.ui.theme.Purple30
import com.mj.compose_clean_architecture.ui.theme.Sky
import com.mj.compose_clean_architecture.ui.theme.White
import com.mj.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeContract.State,
    effectFlow: Flow<HomeContract.Effect>?,
    onEventSent: (event: HomeContract.Event) -> Unit,
    onNavigationRequested: (effect: HomeContract.Effect.Navigation) -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val newsPagingItem = state.newsPagingInfo.collectAsLazyPagingItems()

    var query by rememberSaveable { mutableStateOf("") }
    val dataLoadedMsg = stringResource(R.string.home_screen_loaded_message)

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is HomeContract.Effect.DataLoaded -> Toast.makeText(context, dataLoadedMsg, Toast.LENGTH_SHORT).show()
                is HomeContract.Effect.Navigation.ToDetail -> onNavigationRequested(effect)
            }
        }?.collect()
    }

    Column(modifier = modifier) {
        when {
            state.isLoading -> Progress()
            state.isError -> NetworkError { onEventSent(HomeContract.Event.Retry(query)) }
            else -> {
                SearchBox(
                    fm = focusManager,
                    query = query,
                    onQueryChange = { query = it },
                    onSearchClick = { onEventSent(HomeContract.Event.SearchClick(query)) }
                )
                NewsList(
                    newsPagingItem = newsPagingItem,
                    onItemClick = { onEventSent(HomeContract.Event.NewsSelection(it)) },
                )
            }
        }
    }
}

@Composable
private fun SearchBox(
    fm: FocusManager,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = query,
                onValueChange = onQueryChange,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        fm.clearFocus(true)
                        onSearchClick()
                    },
                ),
                textStyle = TextStyle.Default.copy(fontSize = Typography.bodyMedium.fontSize),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    unfocusedTextColor = Purple30,
                    focusedTextColor = Sky,
                ),
                label = {
                    Text(text = stringResource(id = R.string.query_label),) },
            )
        }
    }
}

@Composable
private fun NewsList(
    newsPagingItem: LazyPagingItems<Content>,
    onItemClick: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(newsPagingItem.itemCount) { index ->
            val news = newsPagingItem[index] ?: return@items
            NewsRow(
                newsInfo = news,
                onItemClick = onItemClick,
            )
        }

        newsPagingItem.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = newsPagingItem.loadState.refresh as LoadState.Error
                    item {
                        ErrorMessage(
                            modifier = Modifier.fillParentMaxSize(),
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item { LoadingNextPageItem(modifier = Modifier) }
                }
            }
        }
    }
}

@Composable
private fun NewsRow(
    newsInfo: News.Content,
    onItemClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onItemClick(newsInfo.link) }
    ) {
        HtmlText(
            htmlText = newsInfo.title,
            textStyle = Typography.titleLarge,
            maxLine = 2,
        )

        Spacer(modifier = Modifier.height(8.dp))

        HtmlText(
            htmlText = newsInfo.description,
            textStyle = Typography.bodyMedium,
            maxLine = 10,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = newsInfo.date.applyDateFormat(),
            style = Typography.bodySmall,
            maxLines = 1,
        )
    }
}

@Composable
fun PageLoader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.fetch_data_from_server),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        CircularProgressIndicator(Modifier.padding(top = 10.dp))
    }
}

@Composable
fun LoadingNextPageItem(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = stringResource(id = R.string.network_error_retry_button_text))
        }
    }
}

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    htmlText: String,
    textStyle: TextStyle,
    maxLine: Int,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                ellipsize = TextUtils.TruncateAt.END
                textSize = textStyle.fontSize.value
                val style = when(textStyle.fontWeight?.weight) {
                    FontWeight.Bold.weight -> Typeface.BOLD
                    else -> Typeface.NORMAL
                }
                setTypeface(typeface, style)
                setTextColor(android.graphics.Color.parseColor("#000000"))
                maxLines = maxLine
            }
        },
        update = {
            it.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    )
}

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
        androidx.compose.material.Text(
            text = stringResource(R.string.network_error_title),
            style = Typography.bodyMedium,
            textAlign = TextAlign.Center,
        )

        androidx.compose.material.Text(
            text = stringResource(R.string.network_error_description),
            style = Typography.bodySmall,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
        )

        Button(onClick = { onRetryButtonClick() }) {
            androidx.compose.material.Text(
                text = stringResource(R.string.network_error_retry_button_text).uppercase()
            )
        }
    }
}

@Composable
fun Progress() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@Preview
private fun HomeScreenPreview() {
    ComposeCleanArchitectureTheme {

        val list = mutableListOf(
            Content(
                title = "국내주식 형펀스서 사흘때 자금 순유출",
                description = "국내 주식형 펀드에서 사흘째 자금이 빠져나갔다. 26일 금융투자협회에 따르면 지난 22일 상장지수펀드(ETF)를 제외한 국내 주식형 펀드에서 126억원이 순유출됐다. 472억원이 들어오고 598억원이 펀드...",
                link = "http://app.yonhapnews.co.kr/YNA/Basic/SNS/r.aspx?c=AKR20160926019000008&did=1195m",
                date = "Mon, 26 Sep 2016 07:50:00 +0900".applyDateFormat()
            ),

            Content(
                title = "테스트 뉴스 타이틀로 표현",
                description = "국내 주식형 펀드에서 사흘째 자금이 빠져나갔다. 26일 금융투자협회에 따르면 지난 22일 상장지수펀드(ETF)를 제외한 국내 주식형 펀드에서 126억원이 순유출됐다. 472억원이 들어오고 598억원이 펀드...",
                link = "http://app.yonhapnews.co.kr/YNA/Basic/SNS/r.aspx?c=AKR20160926019000008&did=1195m",
                date = "Mon, 26 Sep 2016 07:50:00 +0900".applyDateFormat()
            )
        )
        HomeScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            state = HomeContract.State(
                newsPagingInfo = MutableStateFlow(PagingData.empty()),
                isLoading = false,
                isError = false,
            ),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {},
        )
    }
}