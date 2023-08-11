package com.ph.fastcam_part2.chap6.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.chap6.DBKey.Companion.DB_CHAT_ROOMS
import com.ph.fastcam_part2.chap6.chatdetail.ChatDetailActivity
import com.ph.fastcam_part2.databinding.Chap6ChatlistBinding

// Fragment(R.layout.chap6_userlist) 이렇게 선언시 oncreateview 를 오버라이딩 하지않아도 된다..!!
class ChatListFragment : Fragment(R.layout.chap6_chatlist) {
    private lateinit var binding: Chap6ChatlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Chap6ChatlistBinding.bind(view)

        val chatListAdapter = ChatListAdapter{chatRoomItem->
            val intent = Intent(context, ChatDetailActivity::class.java)
            intent.putExtra("chatRoomId",chatRoomItem.chatRoomId)
            intent.putExtra("otherUserId",chatRoomItem.otherUserId)
            startActivity(intent)

        }

        binding.chatListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }

        val currentUserId = Firebase.auth.currentUser?.uid ?: return
        val chatRoomsDB = Firebase.database.reference.child(DB_CHAT_ROOMS).child(currentUserId)
        chatRoomsDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatRoomList = snapshot.children.map {
                    it.getValue(ChatRoomItem::class.java)
                }
                chatListAdapter.submitList(chatRoomList)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}