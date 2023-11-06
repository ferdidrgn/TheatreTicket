package com.ferdidrgn.theatreticket.tools.dataBindingHelpers

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ferdidrgn.theatreticket.base.BaseAdapter
import com.ferdidrgn.theatreticket.base.ListAdapterItem
import com.ferdidrgn.theatreticket.tools.downloadFromUrl

object DataBindingUtil {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun downloadImage(view: ImageView, url: Uri?) {
        view.downloadFromUrl(url)
    }

    @BindingAdapter("setAdapter")
    @JvmStatic
    fun setAdapter(
        recyclerView: RecyclerView,
        adapter: BaseAdapter<ViewDataBinding, ListAdapterItem>?
    ) {
        adapter?.let { adapter ->
            recyclerView.adapter = adapter
        }
    }

    @BindingAdapter("setAdapter")
    @JvmStatic
    fun setAdapter(
        vp: ViewPager2,
        adapter: BaseAdapter<ViewDataBinding, ListAdapterItem>?
    ) {
        adapter?.let {
            vp.adapter = it
        }
    }

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("submitList")
    @JvmStatic
    fun submitList(vp: ViewPager2, list: List<ListAdapterItem>?) {
        val adapter = vp.adapter as? BaseAdapter<ViewDataBinding, ListAdapterItem>?
        adapter?.differ?.submitList(list)
    }

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("submitList")
    @JvmStatic
    fun submitList(recyclerView: RecyclerView, list: List<ListAdapterItem>?) {
        val adapter = recyclerView.adapter as? BaseAdapter<ViewDataBinding, ListAdapterItem>?
        adapter?.differ?.submitList(list)
    }
}