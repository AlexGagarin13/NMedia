package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { posts ->
            binding.render(posts)
        }
    }

    private fun ActivityMainBinding.render(posts: List<Post>) {
        for (post in posts) {
            PostBinding.inflate(
                layoutInflater, postsLinearLayout, true
            ).render(post)
        }
    }

    private fun PostBinding.render(post: Post) {
        authorName.text = post.author
        postContent.text = post.content
        published.text = post.published
        likesAmountImage.setImageResource(getLikeIconResId(post.likedByMe))
        likesAmount.text = Utils.formatActivitiesOnPost(post.likes)
        sharedAmount.text = Utils.formatActivitiesOnPost(post.shared)
        viewsAmount.text = Utils.formatActivitiesOnPost(post.viewed)
        likesAmountImage.setOnClickListener {
            viewModel.onLikedClicked(post)
        }
        sharedImage.setOnClickListener {
            viewModel.onShareClicked(post)
        }
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_24 else R.drawable.ic_like_border_24
}