package com.fycus.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentNotificationBinding
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.NotificationsAdapter

class NotificationFragment : BaseFragment() {
    private lateinit var binding: FragmentNotificationBinding
    private var adapter: NotificationsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewNotification.layoutManager = layoutManager
        adapter = NotificationsAdapter(requireActivity())
        binding.recyclerviewNotification.adapter = adapter
    }

}