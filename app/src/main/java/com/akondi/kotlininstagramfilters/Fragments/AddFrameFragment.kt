package com.akondi.kotlininstagramfilters.Fragments


import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.akondi.kotlininstagramfilters.Adapter.FrameAdapter
import com.akondi.kotlininstagramfilters.Interface.AddFrameFragmentListener
import com.akondi.kotlininstagramfilters.Interface.EmojiAdapterListener

import com.akondi.kotlininstagramfilters.R


class AddFrameFragment : BottomSheetDialogFragment(), FrameAdapter.FontAdapterClickListener {
    override fun onFrameItemSelected(frame: Int) {
        frame_selected = frame
     }

    internal lateinit var recycler_frames: RecyclerView
    internal lateinit var btn_add_frame: Button
    internal var listener: AddFrameFragmentListener? = null
    internal lateinit var adapter: FrameAdapter
    internal var frame_selected = -1

    fun setListener(listener: AddFrameFragmentListener) {
        this.listener = listener
    }

    companion object {
        internal var instance: AddFrameFragment? = null
        fun getInstance(): AddFrameFragment {
            if (instance == null)
                instance =
                        AddFrameFragment()
            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_frame, container, false)

        recycler_frames = view.findViewById(R.id.recycler_frames) as RecyclerView
        btn_add_frame = view.findViewById(R.id.btn_add_frame) as Button

        recycler_frames.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_frames.setHasFixedSize(true)
        adapter = FrameAdapter(context!!, this@AddFrameFragment)
        recycler_frames.adapter = adapter

        btn_add_frame.setOnClickListener {
            listener!!.onFrameSelected(frame_selected)
        }

        return view
    }
}
