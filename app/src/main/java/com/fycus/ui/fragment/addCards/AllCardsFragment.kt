package com.fycus.ui.fragment.addCards

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentAllCardsBinding
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.AllCardsAdapter
import com.fycus.ui.adapter.ItemDecorator
import com.fycus.utils.CommonUtils


class AllCardsFragment : BaseFragment() {
    private lateinit var binding: FragmentAllCardsBinding
    private var adapter: AllCardsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_cards, container, false)
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewAll.layoutManager = layoutManager

        when (resources.displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_XHIGH -> {
                //  Toast.makeText(context, "XHDPI", Toast.LENGTH_SHORT).show()
                binding.recyclerviewAll.addItemDecoration(ItemDecorator(-250));
            }
            else -> {
                binding.recyclerviewAll.addItemDecoration(ItemDecorator(-350));
            }
        }

        adapter = AllCardsAdapter(requireActivity())
        binding.recyclerviewAll.adapter = adapter

        adapter!!.setOnItemClickListener(object : AllCardsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val bundle = Bundle()
                val fragment = ViewCardOrientationFragment()
                bundle.putInt("position",20-(position))
                fragment.arguments = bundle
                CommonUtils.setFragment(fragment, false, requireActivity(), R.id.frameContainer)
            }
        })
    }
}