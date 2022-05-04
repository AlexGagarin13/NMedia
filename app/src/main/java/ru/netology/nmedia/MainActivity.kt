package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.ViewModel.PostViewModel
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils



class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.render(post)
        }

        binding.likesAmountImage.setOnClickListener {
            viewModel.onLikedClicked()
        }

        binding.sharedImage.setOnClickListener {
            viewModel.onShareClicked()
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        authorName.text = post.author
        postContent.text = post.content
        published.text = post.published
        likesAmountImage.setImageResource(getLikeIconResId(post.likedByMe))
        likesAmount.text = Utils.formatActivitiesOnPost(post.likes)
        sharedAmount.text = Utils.formatActivitiesOnPost(post.shared)
        viewsAmount.text = Utils.formatActivitiesOnPost(post.viewed)
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_24 else R.drawable.ic_like_border_24
}