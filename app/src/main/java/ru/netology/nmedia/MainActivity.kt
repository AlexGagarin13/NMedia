package ru.netology.nmedia

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.edit
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.activity.PostContentActivity
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.utils.hideKeyBoard
import ru.netology.nmedia.viewModel.PostViewModel
import ru.netology.nmedia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        viewModel.playVideoURL.observe(this) { videoURL ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoURL))
            startActivity(intent)
        }

        val postContentActivityLauncher = registerForActivityResult(
            PostContentActivity.ResultContract
        ) { postContent ->
            postContent ?: return@registerForActivityResult
            viewModel.onSaveButtonClicked(postContent)
        }

        viewModel.navigateToPostContentScreen.observe(this) {
            postContentActivityLauncher.launch(viewModel.currentPost.value?.content)
        }
    }
}

