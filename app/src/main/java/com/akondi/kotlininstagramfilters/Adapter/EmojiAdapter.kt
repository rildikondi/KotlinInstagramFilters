package com.akondi.kotlininstagramfilters.Adapter

import android.content.Context
import android.support.text.emoji.widget.EmojiTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akondi.kotlininstagramfilters.Interface.EmojiAdapterListener
import com.akondi.kotlininstagramfilters.R

class EmojiAdapter(
    private val context: Context,
    private val emojiItemList: List<String>,
    private val listener: EmojiAdapterListener
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EmojiViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.emoji_item, p0, false)

        return EmojiViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return emojiItemList.size
    }

    override fun onBindViewHolder(p0: EmojiViewHolder, p1: Int) {
        p0.emoji_text_view.setText(emojiItemList[p1])
    }

    inner class EmojiViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        internal var emoji_text_view: EmojiTextView

        init {
            emoji_text_view = itemView.findViewById(R.id.emoji_text_view) as EmojiTextView
            itemView.setOnClickListener {
                listener.onEmojiItemSelected(emojiItemList[adapterPosition])
            }
        }
    }
}

