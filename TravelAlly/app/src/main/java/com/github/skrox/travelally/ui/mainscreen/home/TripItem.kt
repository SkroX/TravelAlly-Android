package com.github.skrox.travelally.ui.mainscreen.home

import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.databinding.ItemTripBinding
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem

class TripItem(public val trip: Trip) :BindableItem<ItemTripBinding>(){

    override fun getLayout() = R.layout.item_trip

    override fun bind(viewBinding: ItemTripBinding, position: Int) {
        viewBinding.trip=trip
    }

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount/3
}