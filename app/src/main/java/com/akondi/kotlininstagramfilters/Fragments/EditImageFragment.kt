package com.akondi.kotlininstagramfilters.Fragments


import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.akondi.kotlininstagramfilters.Interface.EditImageFragmentListener
import com.akondi.kotlininstagramfilters.R


class EditImageFragment : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener {

    private var listener: EditImageFragmentListener? = null
    var seekBarBrightness: SeekBar? = null
    var seekBarContrast: SeekBar? = null
    var seekBarSaturation: SeekBar? = null

    companion object {
        internal var instance: EditImageFragment? = null
        fun getInstance(): EditImageFragment {
            if (instance == null)
                instance =
                        EditImageFragment()
            return instance!!
        }
    }

    fun resetControls() {
        if (seekBarBrightness != null && seekBarContrast != null && seekBarSaturation != null) {
            seekBarBrightness!!.progress = 100
            seekBarContrast!!.progress = 0
            seekBarSaturation!!.progress = 10
        }
    }


    // OVERRIDDEN METHODS OF SEEKBAR
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        var progress: Int = progress
        if (listener != null) {
            if (seekBar!!.id == R.id.seekBarBrightness) {
                listener!!.onBrightnessChanged(progress - 100)
            } else if (seekBar!!.id == R.id.seekBarContrast) {
                progress += 10
                val floatVal = .10f * progress
                listener!!.onContrastChanged(floatVal)
            } else if (seekBar!!.id == R.id.seekBarSaturation) {
                val floatVal = .10f * progress
                listener!!.onSaturationChanged(floatVal)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        if (listener != null) {
            listener!!.onEditStarted()
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (listener != null) {
            listener!!.onEditCompleted()
        }
    }

    fun setListener(listener: EditImageFragmentListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_edit_image, container, false)

        seekBarBrightness = view.findViewById<SeekBar>(R.id.seekBarBrightness)
        seekBarSaturation = view.findViewById<SeekBar>(R.id.seekBarSaturation)
        seekBarContrast = view.findViewById<SeekBar>(R.id.seekBarContrast)

        seekBarBrightness!!.max = 200
        seekBarBrightness!!.progress = 100

        seekBarContrast!!.max = 20
        seekBarContrast!!.progress = 0

        seekBarSaturation!!.max = 30
        seekBarSaturation!!.progress = 10

        seekBarSaturation!!.setOnSeekBarChangeListener(this)
        seekBarContrast!!.setOnSeekBarChangeListener(this)
        seekBarBrightness!!.setOnSeekBarChangeListener(this)

        return view
    }
}
