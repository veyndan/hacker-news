package com.veyndan.hackernews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val adapter = ItemAdapter()
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hacker-news.firebaseio.com/v0/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val hackerNewsService = retrofit.create<HackerNewsService>()

        hackerNewsService.topStories()
            .subscribeOn(Schedulers.io())
            .flatMapObservable { it.body()!!.toObservable() }
            .flatMapSingle { id -> hackerNewsService.story(id) }
            .map { it.body()!! }
            .observeOn(AndroidSchedulers.mainThread())
            .take(10)
            .toList()
            .subscribe { stories ->
                adapter.stories.addAll(stories)
                adapter.notifyDataSetChanged()
            }
    }
}
