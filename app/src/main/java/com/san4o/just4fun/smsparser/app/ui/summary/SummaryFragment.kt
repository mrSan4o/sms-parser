package com.san4o.just4fun.smsparser.app.ui.summary

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.san4o.just4fun.smsparser.app.R
import com.san4o.just4fun.smsparser.app.databinding.FragmentSummaryBinding
import com.san4o.just4fun.smsparser.app.ui.list.SmsListViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel


class SummaryFragment : Fragment() {


    private val viewModel: SmsListViewModel by sharedViewModel()
    lateinit var binding: FragmentSummaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_summary, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.model = viewModel
    }
}
