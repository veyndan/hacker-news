package com.veyndan.hackernews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.view.*
import okhttp3.HttpUrl
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import java.util.concurrent.TimeUnit

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    val stories = mutableListOf<Story>()

    private val disposables = CompositeDisposable()

    private val prettyTime = PrettyTime()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = ViewHolder(inflater.inflate(R.layout.item, parent, false))
        disposables += holder.containerView.clicks()
            .subscribe {
                val url = stories[holder.adapterPosition].url.toUri()
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(parent.context, url)
            }
        return holder
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        disposables.clear()
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.containerView.context
        val story = stories[position]

        holder.containerView.title.text = story.title
        holder.containerView.url.text = HttpUrl.get(story.url).host()
        holder.containerView.caption.text = context.getString(
            R.string.item_caption,
            story.descendants,
            story.by,
            prettyTime.format(Date(TimeUnit.SECONDS.toMillis(story.time)))
        )
    }

    class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer
}
