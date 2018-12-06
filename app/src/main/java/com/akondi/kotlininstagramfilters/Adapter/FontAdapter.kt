package com.akondi.kotlininstagramfilters.Adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.akondi.kotlininstagramfilters.R
import java.lang.StringBuilder

class FontAdapter(
    internal val context: Context,
    internal val listener: FontAdapter.FontAdapterClickListener
) : RecyclerView.Adapter<FontAdapter.FontViewHolder>() {

    var rowSelected = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FontViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.font_item, p0, false)
        return FontViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return fontList.size
    }

    override fun onBindViewHolder(p0: FontViewHolder, p1: Int) {
        if(rowSelected == p1)
            p0.img_check.visibility = View.VISIBLE
        else
            p0.img_check.visibility = View.INVISIBLE

        val typeFace = Typeface.createFromAsset(context.assets, StringBuilder("fonts/")
            .append(fontList.get(p1)).toString())

        p0.txt_font_name.text = fontList.get(p1)
        p0.txt_font_demo.typeface = typeFace
    }

    internal var fontList: List<String>
    
    init {
        fontList = loadFontList()!!
    }

    private fun loadFontList(): List<String>? {
        var result = ArrayList<String>()
        result.add("Arial.ttf")
        result.add("MonotypeCorsiva.ttf")
        return result
    }

    interface FontAdapterClickListener {
        fun onFontSelected(fontName: String)
    }

    inner class FontViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

        internal var txt_font_demo: TextView
        internal var txt_font_name: TextView
        internal var img_check: ImageView

        init {
            txt_font_demo = itemView.findViewById(R.id.txt_font_demo) as TextView
            txt_font_name = itemView.findViewById(R.id.txt_font_name) as TextView

            img_check = itemView.findViewById(R.id.img_check) as ImageView

            itemView.setOnClickListener {
                listener.onFontSelected(fontList[adapterPosition])
                rowSelected = adapterPosition
                notifyDataSetChanged()
            }
        }
    }
}