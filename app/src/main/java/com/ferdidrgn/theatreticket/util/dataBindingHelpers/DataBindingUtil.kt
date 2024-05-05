package com.ferdidrgn.theatreticket.util.dataBindingHelpers

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ferdidrgn.theatreticket.util.base.BaseAdapter
import com.ferdidrgn.theatreticket.util.base.ListAdapterItem
import com.ferdidrgn.theatreticket.util.components.CustomToolbar
import com.ferdidrgn.theatreticket.util.downloadFromUrl

object DataBindingUtil {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun downloadImage(view: ImageView, url: String?) {
        view.downloadFromUrl(url)
    }

    @JvmStatic
    @BindingAdapter(
        value = ["changeText", "isTextClickable"],
        requireAll = false
    )
    fun changeText(
        view: TextView,
        text: String?,
        boolean: Boolean?
    ) {
        if (boolean != null) {
            view.isClickable = boolean
        }
        view.text = text
    }

    @JvmStatic
    @BindingAdapter(
        value = ["cst_tb_back_icon_visibility"],
        requireAll = false
    )
    fun changeVisibility(
        view: CustomToolbar,
        boolean: Boolean?
    ) {
        if (boolean != null) {
            view.visibilityOfBackIcon(boolean)
        }
    }

    //RECYCLERVIEW
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

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("submitList")
    @JvmStatic
    fun submitList(recyclerView: RecyclerView, list: List<ListAdapterItem>?) {
        val adapter = recyclerView.adapter as? BaseAdapter<ViewDataBinding, ListAdapterItem>?
        adapter?.differ?.submitList(list)
    }


    //VIEWPAGER
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
}