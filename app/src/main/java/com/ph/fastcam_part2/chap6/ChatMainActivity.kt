package com.ph.fastcam_part2.chap6

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.chap6.userlist.UserFragment
import com.ph.fastcam_part2.databinding.Chap6Binding

class ChatMainActivity : AppCompatActivity() {
    private lateinit var binding: Chap6Binding
    private val userListFragment = UserFragment()
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding= Chap6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            return
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.userList ->{
                    replaceFragment(userListFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.chatroomList ->{
                    return@setOnItemSelectedListener true
                }
                R.id.myPage ->{
                    return@setOnItemSelectedListener true
                }
                else ->{
                    return@setOnItemSelectedListener false
                }
            }
        }
    }
    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.frameLayout,fragment)
                commit()
            }
    }

}