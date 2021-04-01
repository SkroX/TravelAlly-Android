package com.github.skrox.travelally.ui.mainscreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.databinding.ItemTripBinding
import com.github.skrox.travelally.databinding.ItemTripPopularBinding
import kotlin.random.Random

class ChildAdapter() :
    ListAdapter<Trip, ChildAdapter.ViewHolder>(Companion) {

    var images = mutableListOf<String>("https://www.planetware.com/photos-large/I/italy-colosseum-day.jpg","https://trak.in/wp-content/uploads/2010/04/TajMahal.jpg","https://i0.wp.com/tripadvisor.wpengine.com/wp-content/uploads/2018/12/eiffel-tower-worlds-most-popular-attraction.jpeg?quality=90&w=627","https://s3.india.com/travel/wp-content/uploads/2017/03/gangtok1.jpg","https://media.timeout.com/images/101240669/image.jpg","https://www.intermilesresources.com/iwov-resources/images/blog/20-best-places-to-visit-in-sikkim/Visit-in-Sikkim-Mobile-414x233.jpg")

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
        val binding = ItemTripPopularBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTrip = getItem(position)
        val rand = Random(System.nanoTime())
        currentTrip.image = images[(0..5).random(rand)]
        holder.binding.trip = currentTrip
        holder.binding.executePendingBindings()
    }

    inner class ViewHolder(val binding: ItemTripPopularBinding) : RecyclerView.ViewHolder(binding.root)
}