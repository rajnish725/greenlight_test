package com.fycus.ui.fragment.addCards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentViewCardOrientationBinding
import com.fycus.ui.activity.HomeActivity

class ViewCardOrientationFragment : BaseFragment() {
    private lateinit var binding: FragmentViewCardOrientationBinding
    var positionCheck = 0
    private var orientationListAdapter: ArrayAdapter<String>? = null
    var orientationType: Array<String> = arrayOf("Horizontal", "Vertical")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_view_card_orientation,
            container,
            false
        )
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        positionCheck = this.arguments!!.getInt("position", 0)
        binding.tvTitle.text = "Card Template $positionCheck"
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
    }
}