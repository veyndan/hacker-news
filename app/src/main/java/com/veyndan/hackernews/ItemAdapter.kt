package com.veyndan.hackernews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.view.*
import okhttp3.HttpUrl
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import java.util.concurrent.TimeUnit

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    val stories = mutableListOf<Story>()

    private val prettyTime = PrettyTime()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
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

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}
