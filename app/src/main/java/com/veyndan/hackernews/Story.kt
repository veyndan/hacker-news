package com.veyndan.hackernews

data class Story(
    val by: String,
    val descendants: Int,
    val id: Int,
    val kids: List<Int>,
    val score: Int,
    private val text: String?,
    val time: Long,
    val title: String,
    val type: String,
    private val url: String?
) {

    fun content() = when {
        url != null -> Content.Url(url)
        text != null -> Content.Text(text)
        else -> error("Both url and text are null.")
    }
}
