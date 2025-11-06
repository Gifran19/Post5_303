package com.farhan.post5_303

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Setup story
        val rvStories = findViewById<RecyclerView>(R.id.rv_stories)
        storyAdapter = StoryAdapter(
            listOf(
                Story("intan_dwi", R.drawable.avatar1),
                Story("minda_04", R.drawable.avatar2),
                Story("rubi_community", R.drawable.avatar3),
                Story("rizka", R.drawable.avatar4),
                Story("amel", R.drawable.avatar5)
            )
        )
        rvStories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvStories.adapter = storyAdapter

        // ✅ Setup post feed
        val rvPosts = findViewById<RecyclerView>(R.id.rv_posts)
        postAdapter = PostAdapter(
            onItemClick = { post ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("post_username", post.username)
                intent.putExtra("post_caption", post.caption)
                intent.putExtra("post_image", post.imageUri)
                startActivity(intent)
            },
            onDeleteClick = { post ->
                // Tambahkan popup konfirmasi hapus di sini
                AlertDialog.Builder(this)
                    .setTitle("Hapus Postingan")
                    .setMessage("Apakah kamu yakin ingin menghapus postingan ini?")
                    .setPositiveButton("Ya") { _, _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            AppDatabase.getDatabase(applicationContext).postDao().delete(post)
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        )

        rvPosts.layoutManager = LinearLayoutManager(this)
        rvPosts.adapter = postAdapter

        // ✅ Observe data dari Room
        viewModel.allPosts.observe(this) { posts ->
            postAdapter.submitList(posts)
        }
        // ✅ Isi contoh postingan hanya jika database masih kosong
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(applicationContext).postDao()
            val currentData = dao.getAllNow() // function tambahan (lihat di bawah)
            if (currentData.isEmpty()) {
                dao.insert(
                        Post(
                        username = "intan_dwi",
                        caption = "Menikmati senja di pantai 🌅",
                        imageUri = "android.resource://$packageName/${R.drawable.example1}"
                    )
                )

                dao.insert(
                    Post(
                        username = "minda_04",
                        caption = "my trip my adventure ",
                        imageUri = "android.resource://$packageName/${R.drawable.example2}"
                    )
                )

                dao.insert(
                    Post(
                        username = "rubi_community",
                        caption = "Ngopiii sekk bolooo",
                        imageUri = "android.resource://$packageName/${R.drawable.example3}"
                    )
                )
            }
        }

        // ✅ FAB tambah postingan
        findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener {
            startActivity(Intent(this, AddEditPostActivity::class.java))
        }
    }
}
