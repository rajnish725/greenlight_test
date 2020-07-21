package com.fycus.ui.fragment.shareCards

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentShareCardDetailsBinding
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.MyContactListAdapter
import com.fycus.ui.adapter.ReceivedCardPagerAdapter
import com.fycus.ui.adapter.SharedCardUserListAdapter

class ShareCardDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentShareCardDetailsBinding
    private var pagerAdapter: ReceivedCardPagerAdapter? = null
    private var adapter: SharedCardUserListAdapter? = null
    private var orientationListAdapter: ArrayAdapter<String>? = null
    var orientationType: Array<String> = arrayOf("Horizontal", "Vertical")
    private var type = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_share_card_details,
            container,
            false
        )
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setHorizontal()

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewUser.layoutManager = layoutManager
        adapter = SharedCardUserListAdapter(requireActivity())
        binding.recyclerviewUser.adapter = adapter

        adapter!!.setOnItemClickListener(object : SharedCardUserListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val builder = androidx.appcompat.app.AlertDialog.Builder(
                    requireContext(),
                    R.style.AlertDialogTheme
                )
                builder.setMessage("Are you sure to remove this person?")
                builder.setPositiveButton("Yes") { _, _ ->

                }
                builder.setNegativeButton("No") { _, _ ->

                }
                builder.create()
                builder.show()

            }
        })

    }

    private fun setHorizontal() {
        pagerAdapter = ReceivedCardPagerAdapter(requireActivity(), 1)
        binding.viewPager.adapter = pagerAdapter

        binding.indicator.setViewPager(binding.viewPager)

        val density = resources.displayMetrics.density
        //Set circle indicator radius
        binding.indicator.radius = 4 * density

        mainHandler = Handler(Looper.getMainLooper())

        binding.indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {


            override fun onPageSelected(position: Int) {

                Log.e("position", position.toString())

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }

    private fun setVertical() {
        pagerAdapter = ReceivedCardPagerAdapter(requireActivity(), 2)
        binding.viewPagerVertical.adapter = pagerAdapter

        binding.indicator.setViewPager(binding.viewPagerVertical)

        val density = resources.displayMetrics.density
        //Set circle indicator radius
        binding.indicator.radius = 4 * density

        mainHandler = Handler(Looper.getMainLooper())

        binding.indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {


            override fun onPageSelected(position: Int) {

                Log.e("position", position.toString())

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

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
                    setHorizontal()
                    binding.viewPagerVertical.visibility = View.GONE
                    binding.viewPager.visibility = View.VISIBLE
                    type = 1
                    currentPage = 0
                } else {
                    setVertical()
                    currentPage = 0
                    binding.viewPagerVertical.visibility = View.VISIBLE
                    binding.viewPager.visibility = View.GONE
                    type = 2
                }
            }
        }
    }

        override fun onPause() {
            super.onPause()
            mainHandler.removeCallbacks(updateTextTask)
        }

        override fun onResume() {
            super.onResume()
            mainHandler.post(updateTextTask)
        }

        lateinit var mainHandler: Handler

        private val updateTextTask = object : Runnable {
            override fun run() {
                changePosition()
                mainHandler.postDelayed(this, 5000)
            }
        }

        private var currentPage = 0
        private fun changePosition() {
            if (currentPage == 2) {
                currentPage = 0
            }
            if(type == 1) {
                binding.viewPager.setCurrentItem(currentPage++, true)
            }else{
                binding.viewPagerVertical.setCurrentItem(currentPage++, true)
            }
        }

    }
