package com.ph.fastcam_part2.chap6.chatlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.databinding.Chap6ChatlistBinding

// Fragment(R.layout.chap6_userlist) 이렇게 선언시 oncreateview 를 오버라이딩 하지않아도 된다..!!
class ChatListFragment : Fragment(R.layout.chap6_chatlist) {
    private lateinit var binding: Chap6ChatlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Chap6ChatlistBinding.bind(view)

        val chatListAdapter = ChatListAdapter()
        binding.chatListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }

        chatListAdapter.submitList(
            mutableListOf<ChatRoomItem?>().apply {
                add(ChatRoomItem("22","22","33"))
            }
        )
    }
}