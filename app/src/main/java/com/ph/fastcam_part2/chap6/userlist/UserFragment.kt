package com.ph.fastcam_part2.chap6.userlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.databinding.Chap6UserlistBinding

// Fragment(R.layout.chap6_userlist) 이렇게 선언시 oncreateview 를 오버라이딩 하지않아도 된다..!!
class UserFragment : Fragment(R.layout.chap6_userlist) {
    private lateinit var binding: Chap6UserlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Chap6UserlistBinding.bind(view)

        val userListAdapter = UserListAdapter()
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }

        userListAdapter.submitList(
            mutableListOf<UserItem?>().apply {
                add(UserItem("22","22","33"))
            }
        )
    }
}