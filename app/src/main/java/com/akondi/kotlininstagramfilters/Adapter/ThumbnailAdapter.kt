package com.akondi.kotlininstagramfilters.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.akondi.kotlininstagramfilters.Interface.FilterListFragmentListener
import com.akondi.kotlininstagramfilters.R
import com.zomato.photofilters.utils.ThumbnailItem
import kotlinx.android.synthetic.main.thumbnail_list_item.view.*

class ThumbnailAdapter(
    private val context: Context,
    private val thumbnailItemList: List<ThumbnailItem>,
    private val listener: FilterListFragmentListener
) : RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thumbNail: ImageView
        var filterName: TextView

        init {
            thumbNail = itemView.thumbnail
            filterName = itemView.filter_name
        }
    }

    private var selectedIndex = 0

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.thumbnail_list_item, p0, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return thumbnailItemList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        val thumbnailItem = thumbnailItemList[p1]
        p0.thumbNail.setImageBitmap(thumbnailItem.image)
        p0.thumbNail.setOnClickListener {
            listener.onFilterSelected(thumbnailItem.filter)
            selectedIndex = p1
            notifyDataSetChanged()
        }
        p0.filterName.text = thumbnailItem.filterName

        if (selectedIndex == p1)
            p0.filterName.setTextColor(ContextCompat.getColor(context, R.color.filter_label_selected))
        else
            p0.filterName.setTextColor(ContextCompat.getColor(context, R.color.filter_label_normal))
    }
}