package com.farhan.post5_303

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).postDao()

    // ✅ LiveData yang di-observe MainActivity
    val allPosts: LiveData<List<Post>> = dao.getAll()

    // Opsional: fungsi insert lewat ViewModel
    fun insert(post: Post) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(post)
        }
    }

    // Opsional: fungsi delete lewat ViewModel
    fun delete(post: Post) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.delete(post)
        }
    }
}