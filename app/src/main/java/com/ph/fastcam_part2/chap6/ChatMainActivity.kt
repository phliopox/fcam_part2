package com.ph.fastcam_part2.chap6

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.databinding.Chap6Binding

class ChatMainActivity : AppCompatActivity() {
    private lateinit var binding: Chap6Binding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.chap6)
        val currentUser = Firebase.auth.currentUser

        if (currentUser == null) {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            return
        }
    }


}