package com.akondi.kotlininstagramfilters.Fragments


import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.akondi.kotlininstagramfilters.Adapter.ColorAdapter
import com.akondi.kotlininstagramfilters.Adapter.FontAdapter
import com.akondi.kotlininstagramfilters.Interface.AddTextFragmentListener

import com.akondi.kotlininstagramfilters.R
import java.lang.StringBuilder


class AddTextFragment : BottomSheetDialogFragment(), ColorAdapter.ColorSelectedListener,
    FontAdapter.FontAdapterClickListener {
    override fun onFontSelected(fontName: String) {
        this.typeface = Typeface.createFromAsset(
            context!!.assets, StringBuilder("fonts/")
                .append(fontName).toString()
        )
    }

    var colorSelcted: Int = Color.parseColor("#000000")//default is black
    var typeface = Typeface.DEFAULT

    internal var listener: AddTextFragmentListener? = null

    var edt_add_text: EditText? = null
    var recycler_color: RecyclerView? = null
    var recycler_font: RecyclerView? = null
    var btn_done: Button? = null
    var colorAdapter: ColorAdapter? = null
    var fontAdapter: FontAdapter? = null


    fun setListener(listener: AddTextFragmentListener) {
        this.listener = listener
    }

    override fun onColorSelectedListener(color: Int) {
        colorSelcted = color
    }

    companion object {
        internal var instance: AddTextFragment? = null
        fun getInstance(): AddTextFragment {
            if (instance == null)
                instance =
                        AddTextFragment()
            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_text, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        edt_add_text = view.findViewById<EditText>(R.id.edt_add_text)
        btn_done = view.findViewById<Button>(R.id.btn_done)
        recycler_color = view.findViewById<RecyclerView>(R.id.recycler_color)
        recycler_color!!.setHasFixedSize(true)
        recycler_color!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        colorAdapter = ColorAdapter(context!!, this@AddTextFragment)
        recycler_color!!.adapter = colorAdapter

        btn_done!!.setOnClickListener {
            listener!!.onAddTextListener(typeface, edt_add_text!!.text.toString(), colorSelcted)
        }

        recycler_font = view.findViewById<RecyclerView>(R.id.recycler_font)
        recycler_font!!.setHasFixedSize(true)
        recycler_font!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        fontAdapter = FontAdapter(context!!, this@AddTextFragment)
        recycler_font!!.adapter = fontAdapter
    }
}
