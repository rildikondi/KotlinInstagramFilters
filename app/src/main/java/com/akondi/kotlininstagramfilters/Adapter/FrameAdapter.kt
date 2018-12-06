package com.akondi.kotlininstagramfilters.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.akondi.kotlininstagramfilters.R

class FrameAdapter (
    internal val context: Context,
    internal val listener: FrameAdapter.FontAdapterClickListener
) : RecyclerView.Adapter<FrameAdapter.FrameViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FrameViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.frame_item, p0, false)
        return FrameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return frameList.size
    }

    override fun onBindViewHolder(p0: FrameViewHolder, p1: Int) {
        p0.img_frame.setImageResource(frameList[p1])

        if (row_selected == p1)
            p0.img_check.visibility = View.VISIBLE
        else
            p0.img_check.visibility = View.INVISIBLE
    }

    internal var frameList: List<Int>
    internal var row_selected = -1

    init {
        frameList = loadFrameList()
    }

    private fun loadFrameList(): List<Int> {
        val result = ArrayList<Int>()
        result.add(R.drawable.card_1_resize)
        result.add(R.drawable.card_2_resize)
        result.add(R.drawable.card_3_resize)
        result.add(R.drawable.card_4_resize)
        result.add(R.drawable.card5_resize)
        result.add(R.drawable.card_6_resize)
        result.add(R.drawable.card7_resize)
        result.add(R.drawable.card_8_resize)
        result.add(R.drawable.card_9_resize)
        result.add(R.drawable.card_10_resize)
        return result
    }

    interface FontAdapterClickListener {
        fun onFrameItemSelected(frame: Int)
    }

    inner class FrameViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        internal var img_check: ImageView
        internal var img_frame: ImageView

        init {
            img_check = itemView.findViewById(R.id.img_check) as ImageView
            img_frame = itemView.findViewById(R.id.img_frame) as ImageView

            itemView.setOnClickListener {
                listener.onFrameItemSelected(frameList[adapterPosition])
                row_selected = adapterPosition
                notifyDataSetChanged()
            }
        }
    }
}