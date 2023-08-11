package com.ph.fastcam_part2.chap6.chatdetail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ph.fastcam_part2.chap4.User
import com.ph.fastcam_part2.chap6.DBKey.Companion.DB_CHATS
import com.ph.fastcam_part2.chap6.DBKey.Companion.DB_CHAT_ROOMS
import com.ph.fastcam_part2.chap6.DBKey.Companion.DB_USERS
import com.ph.fastcam_part2.chap6.userlist.UserItem
import com.ph.fastcam_part2.databinding.Chap6ChatDetailBinding

class ChatDetailActivity : AppCompatActivity() {
    private lateinit var binding: Chap6ChatDetailBinding

    private var chatRoomId: String = ""
    private var otherUserId: String = ""
    private var myUserId: String = ""
    private var myUserName : String =""
    private val chatItemList = mutableListOf<ChatItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Chap6ChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRoomId = intent.getStringExtra("chatRoomId") ?: return
        otherUserId = intent.getStringExtra("otherUserId") ?: return
        myUserId = Firebase.auth.currentUser?.uid ?: ""
        val chatAdapter = ChatAdapter()

        Firebase.database.reference.child(DB_USERS).child(myUserId).get()
            .addOnSuccessListener {
                val myUserItem = it.getValue(UserItem::class.java)
                myUserName = myUserItem?.username?:""
            }
        Firebase.database.reference.child(DB_USERS).child(otherUserId).get()
            .addOnSuccessListener {
                val otherUserItem = it.getValue(UserItem::class.java)
                chatAdapter.otherUserItem = otherUserItem
            }

        //채팅 가져오기
        Firebase.database.reference.child(DB_CHATS).child(chatRoomId)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val chatItem = snapshot.getValue(ChatItem::class.java)
                    chatItem ?: return
                    chatItemList.add(chatItem)
                    chatAdapter.submitList(chatItemList)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    //메세지 수정 기능 제공 x
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    //삭제 기능 제공x
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    //이동 기능 x
                }

                override fun onCancelled(error: DatabaseError) {
                    //에러 처리 생략
                }

            })

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatDetailActivity)
            adapter = chatAdapter
        }

        binding.sencBtn.setOnClickListener {
            val message = binding.messageEditText.text.toString()
            if (message.isEmpty()) {
                Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val newChatItem = ChatItem(
                message = message,
                userId = myUserId
            )
            //push() 는 고유키  생성,시간순 자동 정렬 됨
            Firebase.database.reference.child(DB_CHATS).child(chatRoomId).push().apply {
                //key는 push 에서 만든 고유 키값
                newChatItem.chatId = key
                setValue(newChatItem)
            }

            val updates: MutableMap<String, Any> = hashMapOf(
                "${DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to message,
                "${DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to message,
                "${DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
                "${DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
                "${DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserName" to myUserName
            )
            Firebase.database.reference.updateChildren(updates)
            binding.messageEditText.text.clear()
        }
    }
}