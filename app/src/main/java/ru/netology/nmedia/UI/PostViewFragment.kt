package ru.netology.nmedia.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostViewFragmentBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class PostViewFragment : Fragment() {

    private val args by navArgs<PostViewFragmentArgs>()

    private val viewModel by viewModels<PostViewModel>()

    private lateinit var singlePost: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return PostViewFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
            with(binding) {

                viewModel.data.value?.let { listOfPosts ->
                    singlePost = listOfPosts.first { post -> post.id == args.postId }
                    render(singlePost)
                }

                viewModel.data.observe(viewLifecycleOwner) { listOfPosts ->
                    if (!listOfPosts.any{post -> post.id == args.postId}) {
                        return@observe
                    }
                    if (listOfPosts.isNullOrEmpty()) {
                        return@observe
                    }
                    singlePost = listOfPosts.first { post -> post.id == args.postId }
                    render(singlePost)
                }

                setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY) { requestKey, bundle ->
                    if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
                    val newPostContent =
                        bundle.getString(PostContentFragment.RESULT_KEY)
                            ?: return@setFragmentResultListener
                    viewModel.onSaveButtonClicked(newPostContent)
                }

                viewModel.navigateToPostContentScreen.observe(viewLifecycleOwner) { initialContent ->
                    val direction =
                        ru.netology.nmedia.UI.PostViewFragmentDirections.fromPostViewToPostEdit(
                            initialContent
                        )
                    findNavController().navigate(direction)
                }

                .setOnClickListener {
                    viewModel.onLikedClicked(singlePost)
                }

                shareMaterialButton.setOnClickListener {
                    viewModel.onShareButtonClicked(singlePost)
                }
                viewModel.sharedPostContent.observe(viewLifecycleOwner) { postContent ->
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
                    PopupMenu(context, binding.postOptionsMaterialButton).apply {
                        inflate(R.menu.post_options)
                        setOnMenuItemClickListener { option ->
                            when (option.itemId) {
                                R.id.deletePostOption -> {
                                    findNavController().popBackStack()
                                    viewModel.onDeleteMenuOptionClicked(singlePost)
                                    true
                                }
                                R.id.editPostOption -> {
                                    viewModel.onEditMenuOptionClicked(singlePost)
                                    true
                                }
                                else -> {
                                    false
                                }
                            }
                        }
                    }
                }

                binding.postOptionsMaterialButton.setOnClickListener {
                    popupMenu.show()
                }

            }
        }.root
    }

    private fun SinglePostFragmentBinding.render(post: Post) {
        authorNameTextView.text = post.author
        postText.text = post.content
        postText.movementMethod = ScrollingMovementMethod()
        dateAndTimeTextView.text = post.published
        likesMaterialButton.text = post.likes.formatNumber()
        likesMaterialButton.isChecked = post.likedByMe
        shareMaterialButton.text = post.shares.formatNumber()
        shareMaterialButton.isChecked = post.sharedByMe
        postViews.text = post.views.toString()

        val urlList = UrlParse.getHyperLinks(postText.text.toString())
        for (link in urlList) {
            if (link.contains("youtube")
                ||
                link.contains("youtu.be")
            ) {
                post.videoUrl = link
            } else {
                post.videoUrl = ""
            }
        }
        if (post.videoUrl.isEmpty()) {
            videoContentGroup.visibility = View.GONE
        } else {
            videoContentGroup.visibility = View.VISIBLE
        }
    }

    companion object {
        const val CALLER_POST = "CallerPost"
    }

}