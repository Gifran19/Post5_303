package com.farhan.post5_303

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class Story(val username: String, val imageRes: Int)

class StoryAdapter(private val stories: List<Story>)
    : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivStory: ImageView = view.findViewById(R.id.iv_story)
        val tvName: TextView = view.findViewById(R.id.tv_story_username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(v)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.tvName.text = story.username
        Glide.with(holder.itemView.context)
            .load(story.imageRes)
            .circleCrop()
            .into(holder.ivStory)
    }

    override fun getItemCount() = stories.size
}
