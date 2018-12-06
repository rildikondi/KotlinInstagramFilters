package com.akondi.kotlininstagramfilters.Interface

import com.zomato.photofilters.imageprocessors.Filter

interface FilterListFragmentListener {
    fun onFilterSelected(filter: Filter)
}