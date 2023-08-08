package com.ph.fastcam_part2.chap6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.databinding.Chap6Binding

class ChatMainFragment : Fragment() {
    private lateinit var binding: Chap6Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Chap6Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = Firebase.auth.currentUser

        if (currentUser == null) {
            findNavController().navigate(R.id.action_chatMainFragment_to_loginFragment)
            return
        }

    }
}