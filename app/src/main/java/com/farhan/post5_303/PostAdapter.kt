package com.farhan.post5_303

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(
    private val onItemClick: (Post) -> Unit,
    private val onDeleteClick: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(v)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.iv_post_image)
        private val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        private val tvCaption: TextView = itemView.findViewById(R.id.tv_caption)
        private val moreButton: ImageView = itemView.findViewById(R.id.iv_more)

        fun bind(post: Post) {
            tvUsername.text = post.username
            tvCaption.text = post.caption

            try {
                val uri = Uri.parse(post.imageUri)
                Glide.with(itemView.context).load(uri).centerCrop().into(ivImage)
            } catch (e: Exception) {
                ivImage.setImageResource(android.R.color.darker_gray)
            }

            // Klik pada postingan → detail
            itemView.setOnClickListener { onItemClick(post) }

            // Klik titik tiga → tampilkan popup menu
            moreButton.setOnClickListener {
                val popup = PopupMenu(itemView.context, moreButton)
                popup.menuInflater.inflate(R.menu.menu_post, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_delete -> {
                            onDeleteClick(post)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }
}
