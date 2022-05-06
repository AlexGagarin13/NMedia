package ru.netology.nmedia.ViewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository

class PostViewModel : ViewModel() {
    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    fun onLikedClicked() = repository.like()

    fun onShareClicked() = repository.share()
}