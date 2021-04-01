package com.github.skrox.travelally.ui.mainscreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.github.skrox.travelally.data.db.entities.HomeHeadingItem
import com.github.skrox.travelally.data.db.entities.ParentModel
import com.github.skrox.travelally.databinding.HomeParentRvBinding
import com.github.skrox.travelally.databinding.HomeTripsHeadingBinding
import com.github.skrox.travelally.databinding.ItemTripBinding
import com.github.skrox.travelally.databinding.ItemTripPopularBinding
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_ALLTRIPS_ITEMS
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_NEARME_ITEMS
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_POPULAR_ITEMS
import kotlinx.android.synthetic.main.home_parent_rv.view.*
import kotlin.random.Random

class ParentAdapter() :
    ListAdapter<ParentModel, RecyclerView.ViewHolder>(Companion) {
    var images = mutableListOf<String>(
        "https://www.planetware.com/photos-large/I/italy-colosseum-day.jpg",
        "https://trak.in/wp-content/uploads/2010/04/TajMahal.jpg",
        "https://i.pinimg.com/236x/0d/91/76/0d9176f10b71f4729d72a8841e1a7a41.jpg",
        "https://s3.india.com/travel/wp-content/uploads/2017/03/gangtok1.jpg",
        "https://media.timeout.com/images/101240669/image.jpg",
        "https://www.intermilesresources.com/iwov-resources/images/blog/20-best-places-to-visit-in-sikkim/Visit-in-Sikkim-Mobile-414x233.jpg"
    )

    private val viewPool = RecyclerView.RecycledViewPool()

    companion object : DiffUtil.ItemCallback<ParentModel>() {
        override fun areItemsTheSame(
            oldItem: ParentModel,
            newItem: ParentModel
        ): Boolean = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: ParentModel,
            newItem: ParentModel
        ): Boolean =
            oldItem.children[0].id == newItem.children[0].id

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }


    private inner class View1ViewHolder(val binding: HomeParentRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val recyclerView: RecyclerView = itemView.rv_child
        fun bind(position: Int) {
            val childAdapter = ChildAdapter()
            if (recyclerView.onFlingListener == null) {
                val helper: SnapHelper = LinearSnapHelper()
                helper.attachToRecyclerView(recyclerView)
            }

            recyclerView.apply {
                layoutManager =
                    LinearLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)
                adapter = childAdapter
                setRecycledViewPool(viewPool)
            }

            childAdapter.submitList(getItem(position).children)

        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.itemViewType == VIEW_TYPE_NEARME_ITEMS) return
        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
            true
    }

    private inner class View2ViewHolder(val binding: ItemTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class View3ViewHolder(val binding: ItemTripPopularBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class HeadingViewHolder(val binding: HomeTripsHeadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_POPULAR_ITEMS) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HomeParentRvBinding.inflate(layoutInflater)
            return View1ViewHolder(binding)
        } else if (viewType == VIEW_TYPE_NEARME_ITEMS) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemTripBinding.inflate(layoutInflater)
            return View2ViewHolder(binding)
        } else if (viewType == VIEW_TYPE_ALLTRIPS_ITEMS) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemTripPopularBinding.inflate(layoutInflater)
            return View3ViewHolder(binding)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HomeTripsHeadingBinding.inflate(layoutInflater)
            return HeadingViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_POPULAR_ITEMS) {
            (holder as View1ViewHolder).binding.executePendingBindings()
            holder.bind(position)
        } else if (getItemViewType(position) == VIEW_TYPE_NEARME_ITEMS) {
            val currentTrip = getItem(position).children[0]
            val rand = Random(System.nanoTime())
            currentTrip.image = images[(0..5).random(rand)]
            (holder as View2ViewHolder).binding.trip = currentTrip
            holder.binding.executePendingBindings()
        } else if (getItemViewType(position) == VIEW_TYPE_ALLTRIPS_ITEMS) {
            val currentTrip = getItem(position).children[0]
            val rand = Random(System.nanoTime())
            currentTrip.image = images[(0..5).random(rand)]
            (holder as View3ViewHolder).binding.trip = currentTrip
            holder.binding.executePendingBindings()
        } else {
            val currentTrip = getItem(position)
            (holder as HeadingViewHolder).binding.headingItem = HomeHeadingItem(currentTrip.title)
            holder.binding.executePendingBindings()
        }
    }

}