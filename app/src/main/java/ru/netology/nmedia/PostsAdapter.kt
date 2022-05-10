package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils

internal class PostsAdapter(
    private val onLikedClicked: (Post) -> Unit,
    private val onShareClicked: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallBack) {

    inner class ViewHolder(
        private val binding:
        PostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post
        init {
            binding.likesAmountImage.setOnClickListener {
                onLikedClicked(post)
            }
            binding.sharedImage.setOnClickListener {
                onShareClicked(post)
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                postContent.text = post.content
                published.text = post.published
                likesAmountImage.setImageResource(getLikeIconResId(post.likedByMe))
                likesAmount.text = Utils.formatActivitiesOnPost(post.likes)
                sharedAmount.text = Utils.formatActivitiesOnPost(post.shared)
                viewsAmount.text = Utils.formatActivitiesOnPost(post.viewed)
            }
        }

        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_liked_24 else R.drawable.ic_like_border_24
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallBack : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}