package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
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
        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
                clearFocus()
                hideKeyBoard()
            }
        }

        binding.cancelButton.setOnClickListener {
            viewModel.onButtonCancelClicked()
        }

        viewModel.currentPost.observe(this) { currentPost ->

            with(binding) {
                val content = currentPost?.content
                contentEditText.setText(content)
                editableText.hint = content
                if (content != null) {
                    contentEditText.requestFocus()
                    contentEditText.hideKeyBoard()
                    editGroup.visibility = View.VISIBLE
                } else {
                    contentEditText.clearFocus()
                    contentEditText.hideKeyBoard()
                    editGroup.visibility = View.GONE
                }
            }
        }
    }
}