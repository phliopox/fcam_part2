package com.ph.fastcam_part2.chap6.chatdetail

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ph.fastcam_part2.chap6.chatlist.ChatRoomItem
import com.ph.fastcam_part2.chap6.userlist.UserItem
import com.ph.fastcam_part2.databinding.Chap6ItemChatBinding

class ChatAdapter : ListAdapter<ChatItem, ChatAdapter.ViewHolder>(differ) {

    private lateinit var binding : Chap6ItemChatBinding
    var otherUserItem : UserItem? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = Chap6ItemChatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding : Chap6ItemChatBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ChatItem){
            if(item.userId == otherUserItem?.userId) {
                binding.usernameTextView.isVisible = true
                binding.usernameTextView.text= otherUserItem?.username
                binding.messageTextView.text = item.message
                binding.messageTextView.gravity = Gravity.START
            }else{
                binding.usernameTextView.isVisible = false
                binding.messageTextView.text = item.message
                binding.messageTextView.gravity = Gravity.END
            }

        }
    }
    companion object{
        val differ = object : DiffUtil.ItemCallback<ChatItem>(){
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}