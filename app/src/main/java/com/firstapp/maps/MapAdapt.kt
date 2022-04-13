package com.firstapp.maps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.maps.databinding.ItemTodoBinding

class MapAdapt(var map: ArrayList<MapDataformat>): RecyclerView.Adapter<MapAdapt.UserViewHolder>() {
    inner class UserViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = map.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.apply {
            val map = map[position]
            title.text = map.Country
            subtitle.text = map.Street
        }
    }

}


