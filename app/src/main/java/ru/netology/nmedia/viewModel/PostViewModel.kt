package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    // region PostInteractionListener

    override fun onLikedClicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) = repository.share(post.id)

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)


    //endregion
}