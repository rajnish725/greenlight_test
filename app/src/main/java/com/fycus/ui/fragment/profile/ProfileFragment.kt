package com.fycus.ui.fragment.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentProfileBinding
import com.fycus.models.UserModel
import com.fycus.ui.activity.AddContactsFamilyBusinessAndFriendActivity
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.ProfilePagerAdapter
import com.fycus.ui.fragment.addCards.AllCardsFragment
import com.fycus.ui.fragment.favourite.MyFavouriteCardsFragment
import com.fycus.utils.CommonUtils
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class ProfileFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    private var pagerAdapter: ProfilePagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        (requireActivity() as HomeActivity).changeIcon(false)
        (requireActivity() as HomeActivity).bottomSelected(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        pagerAdapter = ProfilePagerAdapter(requireActivity())
        binding.viewPager.adapter = pagerAdapter

        binding.indicator.setViewPager(binding.viewPager)

        val density = resources.displayMetrics.density
        //Set circle indicator radius
        binding.indicator.radius = 4 * density

        mainHandler = Handler(Looper.getMainLooper())

        binding.indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {


            override fun onPageSelected(position: Int) {

                Log.e("position", position.toString())
                currentPage = position
                binding.viewPager.setCurrentItem(currentPage++, true)

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

        pagerAdapter!!.setOnItemClickListener(object : ProfilePagerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (position == 0) {
                    CommonUtils.setFragment(
                        AllCardsFragment(),
                        false,
                        requireActivity(),
                        R.id.frameContainer
                    )
                } else {
                    Toast.makeText(requireActivity(), "coming soon....", Toast.LENGTH_SHORT).show()
                }
            }
        })
        callGetProfileAPI()
    }

    private fun callGetProfileAPI() {
        apiService.callGetProfileAPI("Bearer "+userPref.user.token
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    // utils.simpleAlert(this, "", it.msg.toString())
                    if (commonResponse.status == 1) {
                        setData(commonResponse.mData)
                    } else {
                        Toast.makeText(requireActivity(),commonResponse.message, Toast.LENGTH_SHORT).show()
                    }
                }, {
                    hideProgressDialog()
                    if (it is ConnectException) {
                        utils.simpleAlert(
                            requireActivity(),
                            getString(R.string.error),
                            getString(R.string.check_network_connection)
                        )
                    } else {
                        utils.simpleAlert(requireActivity(), "", it.message.toString())
                    }
                }
            )
    }

    private fun setData(mData: UserModel?) {
        try {
            binding.tvName.text = mData!!.name
            binding.tvEmail.text = mData.email
            binding.tvMobile.text = mData.mobile
            if(mData.image != null)
            Glide.with(requireActivity()).load(Uri.parse(mData.image))
                .error(R.drawable.user)
                .into(binding.ivUser)
        }catch (e : Exception){

        }

    }

    private fun setListener() {
        binding.ivEditProfile.setOnClickListener(this)
        binding.tvFavCards.setOnClickListener(this)
        binding.tvFamily.setOnClickListener(this)
        binding.tvBusiness.setOnClickListener(this)
        binding.tvFriends.setOnClickListener(this)
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
        val intent = Intent(
            requireActivity(),
            AddContactsFamilyBusinessAndFriendActivity::class.java
        )
        when (v!!.id) {
            R.id.ivEditProfile -> {
                CommonUtils.setFragment(
                    EditProfileFragment(),
                    false,
                    requireActivity(),
                    R.id.frameContainer
                )
            }
            R.id.tvFavCards -> {
                CommonUtils.setFragment(
                    MyFavouriteCardsFragment(),
                    false,
                    requireActivity(),
                    R.id.frameContainer
                )
            }
            R.id.tvFamily -> {
                intent.putExtra("type","1")
                startActivity(intent)
            }
            R.id.tvBusiness -> {
                intent.putExtra("type","2")
                startActivity(intent)
            }
            R.id.tvFriends -> {
                intent.putExtra("type","3")
                startActivity(intent)
            }
        }
    }
}