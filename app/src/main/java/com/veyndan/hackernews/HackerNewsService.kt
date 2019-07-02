package com.veyndan.hackernews

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsService {

    @GET("topstories.json")
    fun topStories(): Single<Response<List<Int>>>

    @GET("item/{id}.json")
    fun story(@Path("id") id: Int): Single<Response<Story>>
}
