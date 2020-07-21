package com.fycus.ui.fragment.favourite

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentMyFavouriteCardsBinding
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.ItemDecorator
import com.fycus.ui.adapter.MyFavouriteCardsAdapter
import com.fycus.utils.CommonUtils

class MyFavouriteCardsFragment : BaseFragment() {
    private lateinit var binding: FragmentMyFavouriteCardsBinding
    private var adapter: MyFavouriteCardsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_my_favourite_cards,
            container,
            false
        )
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewFavouriteCards.layoutManager = layoutManager

        when (resources.displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_XHIGH -> {
                //  Toast.makeText(context, "XHDPI", Toast.LENGTH_SHORT).show()
                binding.recyclerviewFavouriteCards.addItemDecoration(ItemDecorator(-250));
            }
            else -> {
                binding.recyclerviewFavouriteCards.addItemDecoration(ItemDecorator(-350));
            }
        }

        adapter = MyFavouriteCardsAdapter(requireActivity())
        binding.recyclerviewFavouriteCards.adapter = adapter

        adapter!!.setOnItemClickListener(object : MyFavouriteCardsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                when (view.id) {
                    R.id.ivImage -> {
                        val bundle = Bundle()
                        val fragment = ViewFavouriteCardsFragment()
                        //bundle.putInt("position",20-(position))
                        //fragment.arguments = bundle
                        CommonUtils.setFragment(
                            fragment,
                            false,
                            requireActivity(),
                            R.id.frameContainer
                        )
                    }
                    R.id.ivRemove -> {
                        val builder = androidx.appcompat.app.AlertDialog.Builder(
                            requireContext(),
                            R.style.AlertDialogTheme
                        )
                        builder.setMessage("Are you sure to remove this card?")
                        builder.setPositiveButton("Yes") { _, _ ->

                        }
                        builder.setNegativeButton("No") { _, _ ->

                        }
                        builder.create()
                        builder.show()
                    }
                }
            }
        })
    }

}