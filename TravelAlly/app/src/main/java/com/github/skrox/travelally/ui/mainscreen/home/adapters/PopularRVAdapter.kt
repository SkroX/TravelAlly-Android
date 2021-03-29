package com.github.skrox.travelally.ui.mainscreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.databinding.ItemTripBinding

class PopularRVAdapter : ListAdapter<Trip, PopularRVAdapter.TripViewHolder>(Companion) {

    class TripViewHolder(val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean =
            oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTripBinding.inflate(layoutInflater)
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val currentTrip = getItem(position)
        holder.binding.trip = currentTrip
        holder.binding.executePendingBindings()
    }
}