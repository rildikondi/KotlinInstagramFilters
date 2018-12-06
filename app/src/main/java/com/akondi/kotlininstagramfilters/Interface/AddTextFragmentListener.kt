package com.akondi.kotlininstagramfilters.Interface

import android.graphics.Typeface

interface AddTextFragmentListener {
    fun onAddTextListener(typeface: Typeface ,text: String, color: Int)
}