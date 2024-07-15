package com.mj.compose_clean_architecture.ui.common

import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

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
                setTextColor(Color.parseColor("#000000"))
                maxLines = maxLine
            }
        },
        update = {
            it.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    )
}