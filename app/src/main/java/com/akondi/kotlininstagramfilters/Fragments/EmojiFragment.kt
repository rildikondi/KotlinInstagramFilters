package com.akondi.kotlininstagramfilters.Fragments


import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akondi.kotlininstagramfilters.Adapter.EmojiAdapter
import com.akondi.kotlininstagramfilters.Interface.EmojiAdapterListener
import com.akondi.kotlininstagramfilters.Interface.EmojiFragmentListener
import com.akondi.kotlininstagramfilters.R
import ja.burhanrashid52.photoeditor.PhotoEditor


class EmojiFragment : BottomSheetDialogFragment(), EmojiAdapterListener {
    override fun onEmojiItemSelected(emoji: String) {
        listener!!.onEmojiSelected(emoji)
    }

    internal var recycler_emoji: RecyclerView? = null
    internal var listener: EmojiFragmentListener? = null

    fun setListener(listener: EmojiFragmentListener) {
        this.listener = listener
    }

    companion object {
        internal var instance: EmojiFragment? = null
        fun getInstance(): EmojiFragment {
            if (instance == null)
                instance =
                        EmojiFragment()
            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_emoji, container, false)

        recycler_emoji = itemView.findViewById(R.id.recycler_emoji) as RecyclerView
        recycler_emoji!!.setHasFixedSize(true)
        recycler_emoji!!.layoutManager = GridLayoutManager(activity, 5)

        val adapter = EmojiAdapter(context!!, PhotoEditor.getEmojis(context), this)
        recycler_emoji!!.adapter = adapter

        return itemView
    }
}
