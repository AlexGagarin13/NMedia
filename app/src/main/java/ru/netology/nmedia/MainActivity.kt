package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils
import kotlin.math.floor


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likes = 5099,
            likedByMe = false,
            shared = 9999,
            viewed = 1_999_999
        )

        binding.render(post)
        binding.likesAmountImage.setOnClickListener {
            post.likedByMe = !post.likedByMe
            binding.likesAmountImage.setImageResource(getLikeIconResId(post.likedByMe))
            if (post.likedByMe) post.likes++ else post.likes--
            binding.likesAmount.text = Utils.formatActivitiesOnPost(post.likes)
        }

        binding.sharedImage.setOnClickListener {
            post.shared++
            binding.sharedAmount.text = Utils.formatActivitiesOnPost(post.shared)
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