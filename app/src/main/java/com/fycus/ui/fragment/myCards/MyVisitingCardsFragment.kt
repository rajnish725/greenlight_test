package com.fycus.ui.fragment.myCards

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentMyVisitingCardsBinding
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.AllCardsAdapter
import com.fycus.ui.adapter.ItemDecorator
import com.fycus.ui.adapter.MyVisitingCardsAdapter
import com.fycus.ui.fragment.addCards.ViewCardOrientationFragment
import com.fycus.utils.CommonUtils

class MyVisitingCardsFragment : BaseFragment() {
    private lateinit var binding : FragmentMyVisitingCardsBinding
    private var adapter : MyVisitingCardsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_visiting_cards, container, false)
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewMyCards.layoutManager = layoutManager

        when (resources.displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_XHIGH -> {
                //  Toast.makeText(context, "XHDPI", Toast.LENGTH_SHORT).show()
                binding.recyclerviewMyCards.addItemDecoration(ItemDecorator(-250));
            }
            else -> {
                binding.recyclerviewMyCards.addItemDecoration(ItemDecorator(-350));
            }
        }

        adapter = MyVisitingCardsAdapter(requireActivity())
        binding.recyclerviewMyCards.adapter = adapter

        adapter!!.setOnItemClickListener(object : MyVisitingCardsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val bundle = Bundle()
                val fragment = ViewMyVisitingCardFragment()
               /* bundle.putInt("position",20-(position))
                fragment.arguments = bundle*/
                CommonUtils.setFragment(fragment, false, requireActivity(), R.id.frameContainer)
            }
        })
    }

}