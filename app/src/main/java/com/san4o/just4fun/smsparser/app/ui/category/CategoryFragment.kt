package com.san4o.just4fun.smsparser.app.ui.category


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.utils.ListAdapter
import kotlinx.android.synthetic.main.fragment_category.*


class CategoryFragment : Fragment(), ListAdapter<CategoryItem> {
    override fun getItem(i: Int): CategoryItem = CategoryItem(1, "name", 111.0)

    override fun getSize(): Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemsContainer.adapter = CategoryAdapter(context!!, this)
        itemsContainer.layoutManager = LinearLayoutManager(context!!)
    }

}
