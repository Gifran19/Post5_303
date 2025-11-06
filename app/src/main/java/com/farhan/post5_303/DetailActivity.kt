package com.farhan.post5_303


import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val iv = findViewById<ImageView>(R.id.iv_detail_image)
        val tvUser = findViewById<TextView>(R.id.tv_detail_username)
        val tvCaption = findViewById<TextView>(R.id.tv_detail_caption)

        val username = intent.getStringExtra("post_username") ?: ""
        val caption = intent.getStringExtra("post_caption") ?: ""
        val image = intent.getStringExtra("post_image") ?: ""

        tvUser.text = username
        tvCaption.text = caption

        try {
            Glide.with(this).load(Uri.parse(image)).centerCrop().into(iv)
        } catch (e: Exception) {
            iv.setImageResource(android.R.color.darker_gray)
        }
    }
}