package com.fycus.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentPurchaseCardsBinding
import com.fycus.ui.activity.HomeActivity

class PurchaseCardsFragment : BaseFragment() {
    private lateinit var binding : FragmentPurchaseCardsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_cards, container, false)
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }
}