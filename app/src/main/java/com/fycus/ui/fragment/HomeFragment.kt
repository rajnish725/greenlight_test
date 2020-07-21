package com.fycus.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentHomeBinding
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.activity.SearchActivity
import com.fycus.ui.adapter.HomeAdapter
import com.fycus.ui.adapter.ItemDecorator
import com.fycus.utils.CommonUtils

class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private var adapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        (requireActivity() as HomeActivity).changeIcon(false)
        (requireActivity() as HomeActivity).bottomSelected(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewHome.layoutManager = layoutManager

        when (resources.displayMetrics.densityDpi) {
            /* DisplayMetrics.DENSITY_LOW -> Toast.makeText(context, "LDPI", Toast.LENGTH_SHORT).show()
             DisplayMetrics.DENSITY_MEDIUM -> Toast.makeText(context, "MDPI", Toast.LENGTH_SHORT)
                 .show()
             DisplayMetrics.DENSITY_HIGH -> //Toast.makeText(context, "HDPI", Toast.LENGTH_SHORT).show()*/
            DisplayMetrics.DENSITY_XHIGH -> {
                //  Toast.makeText(context, "XHDPI", Toast.LENGTH_SHORT).show()
                binding.recyclerviewHome.addItemDecoration(ItemDecorator(-250));
            }
            else -> {
                binding.recyclerviewHome.addItemDecoration(ItemDecorator(-350));
            }
        }

        adapter = HomeAdapter(requireActivity())
        binding.recyclerviewHome.adapter = adapter
        adapter!!.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                CommonUtils.setFragment(
                    ReceivedCardDetailsFragment(),
                    false,
                    requireActivity(),
                    R.id.frameContainer
                )
            }
        })

        binding.ivSearch.setOnClickListener {
            startActivity(Intent(requireActivity(),SearchActivity::class.java))
        }

    }
}