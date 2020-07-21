package com.fycus.ui.fragment.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentViewFavouriteCardsBinding
import com.fycus.ui.activity.HomeActivity

class ViewFavouriteCardsFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentViewFavouriteCardsBinding
    private var orientationListAdapter: ArrayAdapter<String>? = null
    var orientationType: Array<String> = arrayOf("Horizontal", "Vertical")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_view_favourite_cards,
            container,
            false
        )
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    private fun setListener() {
        binding.spOrientation.onItemClickListener
        orientationListAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, orientationType!!)
        orientationListAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spOrientation.adapter = orientationListAdapter

        binding.spOrientation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                if (position == 0) {
                    binding.llHorizontal.visibility = View.GONE
                    binding.llVertical.visibility = View.VISIBLE
                } else {
                    binding.llHorizontal.visibility = View.VISIBLE
                    binding.llVertical.visibility = View.GONE
                }
            }
        }

        binding.tvRemove.setOnClickListener(this)
        binding.btnEdit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvRemove -> {
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
            R.id.btnEdit -> {

            }
        }
    }
}