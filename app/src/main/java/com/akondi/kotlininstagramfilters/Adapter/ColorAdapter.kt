package com.akondi.kotlininstagramfilters.Adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akondi.kotlininstagramfilters.R

import java.util.ArrayList

class ColorAdapter(
    internal val context: Context,
    internal val listener: ColorSelectedListener
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var colorList: List<Int>? = null

    init {
        colorList = genColorList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ColorViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.color_item, viewGroup, false)
        return ColorViewHolder(itemView)
    }

    override fun onBindViewHolder(colorViewHolder: ColorViewHolder, i: Int) {
        colorViewHolder.cardView.setCardBackgroundColor(colorList!![i])
    }

    override fun getItemCount(): Int {
        return colorList!!.size
    }

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var cardView: CardView

        init {

            cardView = itemView.findViewById<View>(R.id.color_section) as CardView
            itemView.setOnClickListener {
                listener.onColorSelectedListener(colorList!![adapterPosition])
            }
        }
    }

    interface ColorSelectedListener {
        fun onColorSelectedListener(color: Int)
    }

    private fun genColorList(): List<Int>? {
        var colorList = ArrayList<Int>()
        colorList.add(Color.parseColor("#131722"))
        colorList.add(Color.parseColor("#ff545e"))
        colorList.add(Color.parseColor("#57bb82"))
        colorList.add(Color.parseColor("#dbeeff"))
        colorList.add(Color.parseColor("#ba5796"))
        colorList.add(Color.parseColor("#bb349b"))
        colorList.add(Color.parseColor("#6e557c"))
        colorList.add(Color.parseColor("#5e40b2"))

        colorList.add(Color.parseColor("#8051cf"))
        colorList.add(Color.parseColor("#895adc"))
        colorList.add(Color.parseColor("#935da0"))
        colorList.add(Color.parseColor("#7a5e93"))
        colorList.add(Color.parseColor("#6c4475"))
        colorList.add(Color.parseColor("#409890"))
        colorList.add(Color.parseColor("#1b36eb"))
        colorList.add(Color.parseColor("#10d6a2"))

        colorList.add(Color.parseColor("#45b9d3"))
        colorList.add(Color.parseColor("#0c6483"))
        colorList.add(Color.parseColor("#487995"))
        colorList.add(Color.parseColor("#428fb9"))
        colorList.add(Color.parseColor("#a183b3"))
        colorList.add(Color.parseColor("#210333"))
        colorList.add(Color.parseColor("#99ffcc"))
        colorList.add(Color.parseColor("#b2b2b2"))

        colorList.add(Color.parseColor("#c0fff4"))
        colorList.add(Color.parseColor("#97ffff"))
        colorList.add(Color.parseColor("#ff1493"))
        colorList.add(Color.parseColor("#caff70"))
        colorList.add(Color.parseColor("#dab420"))
        colorList.add(Color.parseColor("#aa5511"))
        colorList.add(Color.parseColor("#314159"))
        return colorList
    }
}