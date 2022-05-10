package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface PostInteractionListener {
    fun onLikedClicked(post: Post)
    fun onShareClicked(post: Post)
    fun onRemoveClicked(post: Post)
}