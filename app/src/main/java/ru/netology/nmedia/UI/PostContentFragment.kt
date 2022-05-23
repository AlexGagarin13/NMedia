package ru.netology.nmedia.UI


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment() : Fragment() {

    private val args by navArgs<PostContentFragment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        binding.edit.setText(initialContent)
        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            binding.onOkButtonClicked()
        }
    }.root

    private fun PostContentFragmentBinding.onOkButtonClicked() {
        val text = edit.text
        if (!text.isNullOrBlank()) {
            val resulBundle = Bundle(1)
            resulBundle.putString(RESULT_KEY, text.toString())
            setFragmentResult(REQUEST_KEY, resulBundle)
        }
        findNavController().popBackStack()
    }

    companion object {
        private const val INITIAL_CONTENT_ARGUMENTS_KEY = "initialCOntent"

        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "postNewContent"

        fun createInstance(initialContent: String?) = PostContentFragment().apply {
            arguments = createBundle(initialContent)
        }

        fun createBundle(initialContent: String?) =  Bundle(1).apply {
            putString(INITIAL_CONTENT_ARGUMENTS_KEY, initialContent)
        }
    }
}