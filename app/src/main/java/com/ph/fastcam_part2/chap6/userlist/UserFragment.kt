package com.ph.fastcam_part2.chap6.userlist

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
import com.ph.fastcam_part2.chap6.DBKey.Companion.DB_USERS
import com.ph.fastcam_part2.chap6.chatdetail.ChatDetailActivity
import com.ph.fastcam_part2.chap6.chatdetail.ChatItem
import com.ph.fastcam_part2.chap6.chatlist.ChatRoomItem
import com.ph.fastcam_part2.databinding.Chap6UserlistBinding
import java.util.UUID

// Fragment(R.layout.chap6_userlist) 이렇게 선언시 oncreateview 를 오버라이딩 하지않아도 된다..!!
class UserFragment : Fragment(R.layout.chap6_userlist) {
    private lateinit var binding: Chap6UserlistBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Chap6UserlistBinding.bind(view)
        val currentUserId = Firebase.auth.currentUser?.uid ?: ""

        val userListAdapter = UserListAdapter { otherUser ->
            val chatRoomDB = Firebase.database.reference.child(DB_CHAT_ROOMS).child(currentUserId)
                .child(otherUser.userId ?: "")

            chatRoomDB.get().addOnSuccessListener {
                var chatRoomId = ""
                if(it.value!=null){
                    val chatRoom = it.getValue(ChatRoomItem::class.java)
                    chatRoomId = chatRoom?.chatRoomId ?:""
                }else{ // 채팅방 없으면 새로 만들기 !
                    chatRoomId = UUID.randomUUID().toString()
                    val newChatRoom = ChatRoomItem(
                        chatRoomId = chatRoomId,
                        otherUserId = otherUser.userId,
                        otherUserName = otherUser.username
                    )
                    chatRoomDB.setValue(newChatRoom)
                }
                val intent = Intent(context, ChatDetailActivity::class.java)
                intent.putExtra("chatRoomId",chatRoomId)
                intent.putExtra("otherUserId",otherUser.userId)
                startActivity(intent)
            }
        }
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
        Firebase.database.reference.child(DB_USERS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                val userItemList = mutableListOf<UserItem>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    //map 으로 작업하면 바로 list를 submit 할 수 있지만, 로그인한 유저 즉 나는 유저리스트에 없어야 하기 때문에 foreach
                    /*val userlist = snapshot.children.map {
                        it.getValue(UserItem::class.java)
                    }
                    userListAdapter.submitList(userlist)*/

                    snapshot.children.forEach {
                        val user = it.getValue(UserItem::class.java)
                        user ?: return
                        if (user.userId != currentUserId) {
                            userItemList.add(user)
                        }

                    }
                    userListAdapter.submitList(userItemList)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}