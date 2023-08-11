package com.ph.fastcam_part2.chap6.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ph.fastcam_part2.databinding.Chap6ItemChatroomBinding

class ChatListAdapter(private val onClick : (ChatRoomItem)->Unit) : ListAdapter<ChatRoomItem, ChatListAdapter.ViewHolder>(differ) {

    private lateinit var binding : Chap6ItemChatroomBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = Chap6ItemChatroomBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding : Chap6ItemChatroomBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item:ChatRoomItem){
            binding.nicknameTextView.text = item.otherUserName
            binding.lastMessageTextView.text = item.lastMessage

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }
    companion object{
        val differ = object : DiffUtil.ItemCallback<ChatRoomItem>(){
            override fun areItemsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem.chatRoomId == newItem.chatRoomId
            }

            override fun areContentsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}