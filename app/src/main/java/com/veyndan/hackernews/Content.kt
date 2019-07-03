package com.veyndan.hackernews

sealed class Content {

    data class Url(val url: String) : Content()

    data class Text(val text: String) : Content()
}
