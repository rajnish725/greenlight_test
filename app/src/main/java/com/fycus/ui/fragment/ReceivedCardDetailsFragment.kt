package com.fycus.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.fycus.R
import com.fycus.databinding.FragmentReceivedCardDetailsBinding
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.ReceivedCardPagerAdapter

class ReceivedCardDetailsFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentReceivedCardDetailsBinding
    private var pagerAdapter: ReceivedCardPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_received_card_details,
            container,
            false
        )
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        pagerAdapter = ReceivedCardPagerAdapter(requireActivity(),1)
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

    private fun setListener() {
        binding.ivDelete.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            builder.setMessage("Are you sure to delete this card?")
            builder.setPositiveButton("Yes") { _, _ ->

            }
            builder.setNegativeButton("No"){_,_ ->

            }
            builder.create()
            builder.show()

        }


        binding.tvAddAndRemoveFav.setOnClickListener(this)
        binding.ivFav.setOnClickListener(this)
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
        if (currentPage == 3) {
            currentPage = 0
        }
        binding.viewPager.setCurrentItem(currentPage++, true)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tvAddAndRemoveFav ->{
                val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
                builder.setMessage("Are you sure to add to Favourite this card?")
                builder.setPositiveButton("Yes") { _, _ ->

                }
                builder.setNegativeButton("No"){_,_ ->

                }
                builder.create()
                builder.show()
            }
        }
    }

}