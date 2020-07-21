package com.fycus.ui.fragment.shareCards

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentShareCardsBinding
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.ItemDecorator
import com.fycus.ui.adapter.ShareCardsAdapter
import com.fycus.utils.CommonUtils

class ShareCardsFragment : BaseFragment() {
    private lateinit var binding: FragmentShareCardsBinding
    private var adapter: ShareCardsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_share_cards, container, false)
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewShareCards.layoutManager = layoutManager

        when (resources.displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_XHIGH -> {
                //  Toast.makeText(context, "XHDPI", Toast.LENGTH_SHORT).show()
                binding.recyclerviewShareCards.addItemDecoration(ItemDecorator(-250));
            }
            else -> {
                binding.recyclerviewShareCards.addItemDecoration(ItemDecorator(-350));
            }
        }

        adapter = ShareCardsAdapter(requireActivity())
        binding.recyclerviewShareCards.adapter = adapter

        adapter!!.setOnItemClickListener(object : ShareCardsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val bundle = Bundle()
                val fragment = ShareCardDetailsFragment()
                 bundle.putInt("position",5-(position))
                 fragment.arguments = bundle
                CommonUtils.setFragment(fragment, false, requireActivity(), R.id.frameContainer)
            }
        })
    }

}