package com.github.skrox.travelally.ui.mainscreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.databinding.ItemTripBinding

class ChildAdapter() :
    ListAdapter<Trip, ChildAdapter.ViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(
            oldItem: Trip,
            newItem: Trip
        ): Boolean = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: Trip,
            newItem: Trip
        ): Boolean =
            oldItem.id == newItem.id
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTripBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTrip = getItem(position)
        holder.binding.trip = currentTrip
        holder.binding.executePendingBindings()
    }

    inner class ViewHolder(val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root)
}