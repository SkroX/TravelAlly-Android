package com.github.skrox.travelally.ui.mainscreen.home

import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.databinding.ItemTripBinding
import com.github.skrox.travelally.util.loadImage
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder

class TripItem(public val trip: Trip) :BindableItem<ItemTripBinding>(){

    override fun getLayout() = R.layout.item_trip

    override fun bind(viewBinding: ItemTripBinding, position: Int) {
        viewBinding.trip=trip
        viewBinding.executePendingBindings()
    }

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount/2

    override fun unbind(holder: ViewHolder<ItemTripBinding>) {
        Picasso.get().cancelRequest(holder.binding.imageView2)
        holder.binding.imageView2.setImageBitmap(null)
        super.unbind(holder)
    }

}