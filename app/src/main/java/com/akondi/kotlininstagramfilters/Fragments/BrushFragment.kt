package com.akondi.kotlininstagramfilters.Fragments


import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.ToggleButton
import com.akondi.kotlininstagramfilters.Adapter.ColorAdapter
import com.akondi.kotlininstagramfilters.Interface.BrushFragmentListener
import com.akondi.kotlininstagramfilters.R


class BrushFragment : BottomSheetDialogFragment(), ColorAdapter.ColorSelectedListener {
    override fun onColorSelectedListener(color: Int) {
        listener!!.onBrushColorChangedListener(color)
    }

    var seekbar_brush_size: SeekBar? = null
    var seekbar_brush_opacity: SeekBar? = null
    var btn_brush_state: ToggleButton? = null
    var recycler_color: RecyclerView? = null

    var colorAdapter: ColorAdapter? = null

    private var listener: BrushFragmentListener? = null

    fun setListener(listener: BrushFragmentListener) {
        this.listener = listener
    }

    companion object {
        internal var instance: BrushFragment? = null
        fun getInstance(): BrushFragment {
            if (instance == null)
                instance =
                        BrushFragment()
            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_brush, container, false)
        initViews(itemView)
        setupRecyclerColor()
        listenForBrushSizeChanges()
        listenForBrushOpacityChanges()
        listenForBrushStateChanges()
        return itemView
    }

    private fun setupRecyclerColor() {
        recycler_color!!.setHasFixedSize(true)
        recycler_color!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        colorAdapter = ColorAdapter(context!!, this@BrushFragment)
        recycler_color!!.adapter = colorAdapter
    }

    private fun initViews(itemView: View) {
        seekbar_brush_size = itemView.findViewById<SeekBar>(R.id.seekbar_brush_size)
        seekbar_brush_opacity = itemView.findViewById<SeekBar>(R.id.seekbar_brush_opacity)
        btn_brush_state = itemView.findViewById<ToggleButton>(R.id.btn_brush_state)
        recycler_color = itemView.findViewById(R.id.recycler_color)
    }

    private fun listenForBrushStateChanges() {
        btn_brush_state!!.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                listener!!.onBrushStateChangedListener(isChecked)
            }
        })
    }

    private fun listenForBrushOpacityChanges() {
        seekbar_brush_opacity!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener!!.onBrushOpacityChangedListener(progress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun listenForBrushSizeChanges() {
        seekbar_brush_size!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener!!.onBrushSizeChangedListener(progress.toFloat())
            }
        })
    }
}
