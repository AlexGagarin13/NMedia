package ru.netology.nmedia.data.impl

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostBinding

internal class PostsAdapter : RecyclerView.Adapter<>() {
    class ViewHolder(binding: PostBinding):RecyclerView.ViewHolder(binding.root)
}