package com.ph.fastcam_part2.chap4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ph.fastcam_part2.databinding.Chap4ItemResultBinding
import com.ph.fastcam_part2.databinding.Chap4ItemUserBinding
import com.ph.fastcam_part2.databinding.Chap4ResultBinding
class RepoAdapter(private val onClick :(Repo) -> Unit) : ListAdapter<Repo, RepoAdapter.RepoViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding =
            Chap4ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RepoViewHolder(private val binding: Chap4ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Repo) {
            binding.repoNameTextView.text = item.name
            binding.descriptionTextView.text = item.description
            binding.starCountTextView.text = item.starCount.toString()
            binding.forkCountTextView.text = "${item.forkCount}"

            binding.root.setOnClickListener {
                onClick(item)
            }
        }

    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }
}
