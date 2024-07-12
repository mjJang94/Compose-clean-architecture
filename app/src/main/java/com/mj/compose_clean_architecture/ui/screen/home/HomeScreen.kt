package com.mj.compose_clean_architecture.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mj.compose_clean_architecture.R
import com.mj.compose_clean_architecture.common.ktx.applyDateFormat
import com.mj.compose_clean_architecture.data.model.News
import com.mj.compose_clean_architecture.ui.base.SIDE_EFFECTS_KEY
import com.mj.compose_clean_architecture.ui.common.NetworkError
import com.mj.compose_clean_architecture.ui.common.Progress
import com.mj.compose_clean_architecture.ui.theme.ComposeCleanArchitectureTheme
import com.mj.compose_clean_architecture.ui.theme.Typography
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeContract.State,
    effectFlow: Flow<HomeContract.Effect>?,
    onEventSent: (event: HomeContract.Event) -> Unit,
    onNavigationRequested: (effect: HomeContract.Effect.Navigation) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val dataLoadedMsg = stringResource(R.string.home_screen_snackbar_loaded_message)
    var query by remember { mutableStateOf("") }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is HomeContract.Effect.DataLoaded -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = dataLoadedMsg,
                        duration = SnackbarDuration.Short,
                    )
                }

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
                    query = query,
                    onQueryChange = { query = it },
                    onSearchClick = { onEventSent(HomeContract.Event.SearchClick(query)) }
                )
                NewsList(news = state.news, onItemClick = { onEventSent(HomeContract.Event.NewsSelection(it)) })
            }
        }
    }
}

@Composable
private fun SearchBox(
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
                placeholder = { Text(text = stringResource(id = R.string.query_label)) },
                maxLines = 1,
            )

            SearchButton(
                modifier = Modifier.wrapContentSize(),
                onSearchClick = onSearchClick,
            )
        }
    }
}

@Composable
private fun SearchButton(
    modifier: Modifier,
    onSearchClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onSearchClick,
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = "",
            tint = Color.Black
        )
    }
}


@Composable
private fun NewsList(
    news: List<News>,
    onItemClick: (News) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(news) { news ->
            NewsRow(
                news = news,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
private fun NewsRow(
    news: News,
    onItemClick: (News) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onItemClick(news) }
    ) {
        Text(
            text = news.title,
            style = Typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = news.description,
            style = Typography.bodyMedium,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = news.date,
            style = Typography.bodySmall,
            maxLines = 1,
        )
    }
}

@Composable
@Preview
private fun HomeScreenPreview() {
    ComposeCleanArchitectureTheme {

        val list = mutableListOf(
            News(
                title = "국내주식 형펀스서 사흘때 자금 순유출",
                description = "국내 주식형 펀드에서 사흘째 자금이 빠져나갔다. 26일 금융투자협회에 따르면 지난 22일 상장지수펀드(ETF)를 제외한 국내 주식형 펀드에서 126억원이 순유출됐다. 472억원이 들어오고 598억원이 펀드...",
                link = "http://app.yonhapnews.co.kr/YNA/Basic/SNS/r.aspx?c=AKR20160926019000008&did=1195m",
                date = "Mon, 26 Sep 2016 07:50:00 +0900".applyDateFormat()
            ),

            News(
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
                news = list,
                isLoading = false,
                isError = false,
            ),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun NewListRow() {
    ComposeCleanArchitectureTheme {
        val mock = News(
            title = "국내주식 형펀스서 사흘때 자금 순유출",
            description = "국내 주식형 펀드에서 사흘째 자금이 빠져나갔다. 26일 금융투자협회에 따르면 지난 22일 상장지수펀드(ETF)를 제외한 국내 주식형 펀드에서 126억원이 순유출됐다. 472억원이 들어오고 598억원이 펀드...",
            link = "http://app.yonhapnews.co.kr/YNA/Basic/SNS/r.aspx?c=AKR20160926019000008&did=1195m",
            date = "Mon, 26 Sep 2016 07:50:00 +0900".applyDateFormat()
        )
        NewsRow(
            news = mock,
            onItemClick = {},
        )
    }
}