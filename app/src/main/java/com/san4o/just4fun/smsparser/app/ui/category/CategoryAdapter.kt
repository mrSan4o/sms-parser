package com.san4o.just4fun.smsparser.app.ui.category

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.databinding.ViewHolderCategoryItemBinding
import com.san4o.just4fun.smsparser.app.utils.ListAdapter

class CategoryAdapter(
    private val context: Context,
    val items: ListAdapter<CategoryItem>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: ViewHolderCategoryItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.view_holder_category_item,
                p0,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.getSize()


    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        val item = items.getItem(index)
        viewHolder.binding
    }

    class ViewHolder(val binding: ViewHolderCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
