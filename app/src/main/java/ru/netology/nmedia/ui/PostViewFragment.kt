package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostViewFragmentBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.viewModel.PostViewModel

class PostViewFragment : Fragment() {

    private val args by navArgs<PostViewFragmentArgs>()

    private val viewModel by activityViewModels<PostViewModel>()

    private lateinit var singlePost: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostViewFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        with(binding) {

            viewModel.data.observe(viewLifecycleOwner) { listOfPosts ->
                if (!listOfPosts.any { post -> post.id == args.postId }) {
                    return@observe
                }
                if (listOfPosts.isNullOrEmpty()) {
                    return@observe
                }
                singlePost = listOfPosts.first { post -> post.id == args.postId }
                render(singlePost)
            }

            viewModel.navigateToPostContentScreen.observe(viewLifecycleOwner) { initialContent ->
                val direction =
                    ru.netology.nmedia.ui.PostViewFragmentDirections.fromPostViewToPostEdit(
                        initialContent
                    )
                findNavController().navigate(direction)
            }

            buttonLikes.setOnClickListener {
                viewModel.onLikedClicked(singlePost)
            }

            buttonReposts.setOnClickListener {
                viewModel.onShareClicked(singlePost)
            }
            viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, postContent)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            val popupMenu by lazy {
                context?.let {
                    PopupMenu(it, binding.menu).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { option ->
                            when (option.itemId) {
                                R.id.remove -> {
                                    findNavController().popBackStack()
                                    viewModel.onRemoveClicked(singlePost)
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.onEditClicked(singlePost)
                                    true
                                }
                                else -> {
                                    false
                                }
                            }
                        }
                    }
                }
            }
            binding.menu.setOnClickListener { popupMenu?.show() }
        }
    }.root
}

private fun PostViewFragmentBinding.render(post: Post) {
    authorName.text = post.author
    postContent.text = post.content
    published.text = post.published
    buttonLikes.text = Utils.formatActivitiesOnPost(post.likes)
    buttonLikes.isChecked = post.likedByMe
    buttonReposts.text = Utils.formatActivitiesOnPost(post.shared)
    buttonViewsAmount.text = Utils.formatActivitiesOnPost(post.viewed)
    groupVideo.visibility =
        if (post.videoURL.isBlank()) View.GONE else View.VISIBLE
}
