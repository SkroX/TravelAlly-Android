package com.github.skrox.travelally.ui.mainscreen.home

import com.github.skrox.travelally.R
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_expandable_header.view.*


class ExpandableHeaderItem(private val title: String)
    : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.item_expandable_header_title.text = title
        viewHolder.itemView.item_expandable_header_icon.setImageResource(getRotatedIconResId())

        viewHolder.itemView.item_expandable_header_root.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewHolder.itemView.item_expandable_header_icon.setImageResource(getRotatedIconResId())
        }
    }

    override fun getLayout() = R.layout.item_expandable_header

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun getRotatedIconResId() =
        if (expandableGroup.isExpanded)
            R.drawable.ic_arrow_drop_up_black_24dp
        else
            R.drawable.ic_arrow_drop_down_black_24dp
}