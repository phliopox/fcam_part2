package com.ph.fastcam_part2.chap6.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ph.fastcam_part2.databinding.Chap6ItemUserBinding

class UserListAdapter(private val onClick : (UserItem)->Unit ) : ListAdapter<UserItem, UserListAdapter.ViewHolder>(differ) {

    private lateinit var binding : Chap6ItemUserBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = Chap6ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding : Chap6ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item:UserItem){
            binding.nicknameTextView.text = item.username
            binding.descriptionTextView.text = item.description

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }
    companion object{
        val differ = object : DiffUtil.ItemCallback<UserItem>(){
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}