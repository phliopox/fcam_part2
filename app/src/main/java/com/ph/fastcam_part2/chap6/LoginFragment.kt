package com.ph.fastcam_part2.chap6

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.TAG
import com.ph.fastcam_part2.databinding.Chap6LoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding : Chap6LoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Chap6LoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if(email.isEmpty()&&password.isEmpty()){
                Toast.makeText(requireContext(),"이메일 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Firebase.auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        findNavController().navigate(R.id.action_loginFragment_to_chatMainFragment)
                    }else{
                        Log.e(TAG, "onViewCreated: ${task.exception}")
                        Toast.makeText(requireContext(),"로그인 실패", Toast.LENGTH_SHORT).show()

                    }
                }
        }
        binding.signUpBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if(email.isEmpty()&&password.isEmpty()){
                Toast.makeText(requireContext(),"이메일 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.length<6){
                Toast.makeText(requireContext(),"패스워드는 6자 이상 입력해야합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Firebase.auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(requireContext(),"회원가입 성공, 로그인 해주세요", Toast.LENGTH_SHORT).show()

                    }else{
                        Log.d(TAG, "LoginFragment - onViewCreated: ${task.exception}");
                        Toast.makeText(requireContext(),"회원가입 실패", Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }
}