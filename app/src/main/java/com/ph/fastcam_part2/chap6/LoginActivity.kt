package com.ph.fastcam_part2.chap6

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.TAG
import com.ph.fastcam_part2.chap6.DBKey.Companion.DB_USERS
import com.ph.fastcam_part2.databinding.Chap6LoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: Chap6LoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Chap6LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "이메일 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    val currentUser = Firebase.auth.currentUser
                    if (task.isSuccessful && currentUser != null) {
                        // 보통 회원가입하고 firebase 에 데이터를 넣는 작업을 하지만, 여기선 로그인하면 넣는다.

                        val uid = currentUser.uid
                        val user = mutableMapOf<String,Any>()
                        user["userId"] = uid
                        user["username"] = email

                        Firebase.database.reference.child(DB_USERS).child(uid).updateChildren(user)


                        val intent = Intent(this, ChatMainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e(TAG, "onViewCreated: ${task.exception}")
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()

                    }
                }
        }
        binding.signUpBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "이메일 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "패스워드는 6자 이상 입력해야합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "회원가입 성공, 로그인 해주세요", Toast.LENGTH_SHORT).show()

                    } else {
                        Log.d(TAG, "LoginFragment - onViewCreated: ${task.exception}");
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }

}