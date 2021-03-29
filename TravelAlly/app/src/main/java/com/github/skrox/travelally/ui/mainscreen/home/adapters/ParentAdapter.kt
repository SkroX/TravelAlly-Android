package com.github.skrox.travelally.ui.mainscreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.github.skrox.travelally.data.db.entities.ParentModel
import com.github.skrox.travelally.databinding.HomeParentRvBinding
import com.github.skrox.travelally.databinding.ItemTripBinding
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_POPULAR_ITEMS
import kotlinx.android.synthetic.main.home_parent_rv.view.*

class ParentAdapter() :
    ListAdapter<ParentModel, RecyclerView.ViewHolder>(Companion) {

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
        if (holder.itemViewType == VIEW_TYPE_POPULAR_ITEMS)
            (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                true
    }

    private inner class View2ViewHolder(val binding: ItemTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_POPULAR_ITEMS) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HomeParentRvBinding.inflate(layoutInflater)
            return View1ViewHolder(binding)
        }
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTripBinding.inflate(layoutInflater)
        return View2ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItem(position).type == VIEW_TYPE_POPULAR_ITEMS) {
            (holder as View1ViewHolder).binding.executePendingBindings()
            holder.bind(position)
        } else {
            val currentTrip = getItem(position)
            (holder as View2ViewHolder).binding.trip = currentTrip.children[0]
            holder.binding.executePendingBindings()
        }
    }

}