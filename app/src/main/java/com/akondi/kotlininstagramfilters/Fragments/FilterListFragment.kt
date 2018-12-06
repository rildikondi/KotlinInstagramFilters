package com.akondi.kotlininstagramfilters.Fragments


import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akondi.kotlininstagramfilters.Adapter.ThumbnailAdapter
import com.akondi.kotlininstagramfilters.Interface.FilterListFragmentListener
import com.akondi.kotlininstagramfilters.MainActivity
import com.akondi.kotlininstagramfilters.R
import com.akondi.kotlininstagramfilters.Utils.BitmapUtils
import com.akondi.kotlininstagramfilters.Utils.SpaceItemDecoration
import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem
import com.zomato.photofilters.utils.ThumbnailsManager


class FilterListFragment : BottomSheetDialogFragment(), FilterListFragmentListener {

    internal lateinit var recycler_view: RecyclerView
    internal var listener: FilterListFragmentListener? = null
    internal lateinit var adapter: ThumbnailAdapter
    internal lateinit var thumbnailItemList: MutableList<ThumbnailItem>

    companion object {
        internal var instance: FilterListFragment? = null
        internal var bitmap:Bitmap? = null

        fun getInstance(bitmapSave: Bitmap?): FilterListFragment {
            bitmap = bitmapSave
            if(instance == null)
                instance = FilterListFragment()
            return instance!!
        }
    }

    fun setListener(listFragmentListener: FilterListFragmentListener) {
        this.listener = listFragmentListener
    }

    override fun onFilterSelected(filter: Filter) {
        if (listener != null) {
            listener!!.onFilterSelected(filter)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter_list, container, false)

        thumbnailItemList = ArrayList<ThumbnailItem>()
        adapter = ThumbnailAdapter(activity!!, thumbnailItemList, this)

        recycler_view = view!!.findViewById<RecyclerView>(R.id.recycler_view)
        recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.itemAnimator = DefaultItemAnimator()
        val space = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()
        recycler_view.addItemDecoration(SpaceItemDecoration(space))
        recycler_view.adapter = adapter

        displayImage(bitmap)

        return view
    }

    fun displayImage(bitmap: Bitmap?) {
        val r = Runnable {
            val thumbImage: Bitmap?

            if (bitmap == null)
                thumbImage = BitmapUtils.getBitmapFromAssets(activity!!,
                    MainActivity.Main.IMAGE_NAME, 100, 100)
            else
                thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false)

            if (thumbImage == null)
                return@Runnable

            ThumbnailsManager.clearThumbs()
            thumbnailItemList.clear()

            //add normal bitmap first
            val thumbnailItem = ThumbnailItem()
            thumbnailItem.image = thumbImage
            thumbnailItem.filterName = "Normal"
            ThumbnailsManager.addThumb(thumbnailItem)

            //Add Filter pack
            val filters = FilterPack.getFilterPack(activity!!)

            for (filter in filters) {
                val item = ThumbnailItem()
                item.image = thumbImage
                item.filter = filter
                item.filterName = filter.name
                ThumbnailsManager.addThumb(item)
            }

            thumbnailItemList.addAll(ThumbnailsManager.processThumbs(activity))
            activity!!.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
        Thread(r).start()
    }
}
