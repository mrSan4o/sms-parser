package com.san4o.just4fun.smsparser.app.ui.list

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.databinding.FragmentSmsListBinding
import com.san4o.just4fun.smsparser.app.utils.showToastShort
import kotlinx.android.synthetic.main.sms_list.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class SmsListFragment : Fragment() {

    private val viewModel: SmsListViewModel by sharedViewModel()
    private lateinit var binding: FragmentSmsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_sms_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SmsListAdapter(context!!, viewModel)

        binding.model = viewModel

        viewModel.errorLoadingNotification.observe(this, Observer { showToastShort("Error Loading") })
        viewModel.noItemsNotification.observe(this, Observer { showToastShort("No Items FOUND") })
        viewModel.refreshItemsViewCommand.observe(this, Observer { adapter.notifyDataSetChanged() })

        smsList.adapter = adapter
        smsList.layoutManager = LinearLayoutManager(context)
    }
}
