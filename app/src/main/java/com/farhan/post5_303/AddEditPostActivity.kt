package com.farhan.post5_303

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.farhan.post5_303.databinding.ActivityAddEditPostBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditPostBinding
    private var imageUri: Uri? = null

    // ✅ launcher untuk pilih gambar (pakai file picker bawaan Android)
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                imageUri = uri
                // beri izin baca ke depannya
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                // tampilkan preview gambar
                binding.ivPreview.setImageURI(uri)
            } else {
                Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
            }
        }

    // ✅ launcher untuk minta izin akses media
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(this, "Izin akses gambar ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol buka galeri
        binding.btnAddImage.setOnClickListener {
            checkPermissionAndOpenGallery()
        }

        // Tombol simpan post
        binding.btnSave.setOnClickListener {
            savePost()
        }
    }

    // ✅ Cek izin sesuai versi Android
    private fun checkPermissionAndOpenGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(this, "Aktifkan izin akses gambar di pengaturan", Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    // ✅ Fungsi buka galeri / file picker
    private fun openGallery() {
        pickImageLauncher.launch(arrayOf("image/*"))
    }

    // ✅ Simpan data post ke Room
    private fun savePost() {
        val username = binding.etUsername.text.toString()
        val caption = binding.etCaption.text.toString()
        val image = imageUri?.toString() ?: ""

        if (username.isEmpty() || caption.isEmpty() || image.isEmpty()) {
            Toast.makeText(this, "Lengkapi semua data dulu!", Toast.LENGTH_SHORT).show()
            return
        }

        val post = Post(username = username, caption = caption, imageUri = image)

        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(applicationContext).postDao().insert(post)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddEditPostActivity, "Post berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
