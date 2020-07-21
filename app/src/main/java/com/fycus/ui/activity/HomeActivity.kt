package com.fycus.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.fycus.BaseActivity
import com.fycus.R
import com.fycus.databinding.ActivityHomeBinding
import com.fycus.ui.fragment.*
import com.fycus.ui.fragment.addCards.AllCardsFragment
import com.fycus.ui.fragment.myCards.MyVisitingCardsFragment
import com.fycus.ui.fragment.profile.ProfileFragment
import com.fycus.ui.fragment.shareCards.ShareCardsFragment
import com.fycus.utils.CommonUtils

class HomeActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mFragment: Fragment
    private var listener: HomeActivity.OnItemClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setListener()
    }

    private fun setListener() {
        binding.llAppBarDashboard.ivDot.setOnClickListener(this)
        binding.llAppBarDashboard.ivUser.setOnClickListener(this)
        binding.llAppBarDashboard.ivShare.setOnClickListener(this)
        binding.llAppBarDashboard.llSaddleBag.setOnClickListener(this)
        binding.llAppBarDashboard.rlNotifications.setOnClickListener(this)
        mFragment = HomeFragment()

        CommonUtils.setFragment(mFragment, true, this, R.id.frameContainer)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivDot -> {
                showMenuDialog(binding.llAppBarDashboard.ivDot)
            }
            R.id.ivUser -> {
                CommonUtils.setFragment(ProfileFragment(), false, this, R.id.frameContainer)
            }
            R.id.llSaddleBag -> {
                CommonUtils.setFragment(HomeFragment(), true, this, R.id.frameContainer)
            }
            R.id.rlNotifications -> {
                CommonUtils.setFragment(NotificationFragment(), false, this, R.id.frameContainer)
            }
            R.id.ivShare -> {
                if(listener != null){
                    listener!!.onItemClick()
                }
            }
        }
    }

    private fun showMenuDialog(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.home, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_my_card -> {
                    CommonUtils.setFragment(MyVisitingCardsFragment(), false, this, R.id.frameContainer)
                }
                R.id.action_my_contacts -> {
                    CommonUtils.setFragment(MyContactsFragment(), false, this, R.id.frameContainer)
                }
                R.id.action_card_sent -> {
                    CommonUtils.setFragment(ShareCardsFragment(), false, this, R.id.frameContainer)
                }
                R.id.action_purchase_card -> {
                    CommonUtils.setFragment(PurchaseCardsFragment(), false, this, R.id.frameContainer)
                }
                R.id.action_create_card -> {
                    CommonUtils.setFragment(AllCardsFragment(), false, this, R.id.frameContainer)
                }
                R.id.action_my_subscription -> {
                    CommonUtils.setFragment(SubscriptionPlanFragment(), false, this, R.id.frameContainer)
                }
                R.id.action_help -> {
                    CommonUtils.setFragment(HelpFragment(), false, this, R.id.frameContainer)
                }
                R.id.action_faq -> {
                    CommonUtils.setFragment(FaqFragment(), false, this, R.id.frameContainer)
                }
                /*R.id.action_payment -> {
                    Toast.makeText(this, "Payment", Toast.LENGTH_SHORT).show()
                }*/
                R.id.action_logout -> {
                    logoutAlert()
                    //Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popup.show()
    }

    fun bottomSelected(b: Boolean) {
        if (b) {
            binding.llAppBarDashboard.ivSaddle.background =
                this.getDrawable(R.drawable.saddlebag_active)
            binding.llAppBarDashboard.ivUser.background = this.getDrawable(R.drawable.profile)
        } else {
            binding.llAppBarDashboard.ivSaddle.background = this.getDrawable(R.drawable.saddlebag)
            binding.llAppBarDashboard.ivUser.background =
                this.getDrawable(R.drawable.profile_active)
        }
    }

    fun changeIcon(b: Boolean) {
        if (b) {
            binding.llAppBarDashboard.rlBottom.visibility = View.GONE
            binding.llAppBarDashboard.ivLogo.visibility = View.GONE
            binding.llAppBarDashboard.ivAppName.visibility = View.GONE
            binding.llAppBarDashboard.ivDot.visibility = View.GONE
            binding.llAppBarDashboard.llBack.visibility = View.VISIBLE
        } else {
            binding.llAppBarDashboard.rlBottom.visibility = View.VISIBLE
            binding.llAppBarDashboard.ivLogo.visibility = View.VISIBLE
            binding.llAppBarDashboard.ivAppName.visibility = View.VISIBLE
            binding.llAppBarDashboard.ivDot.visibility = View.VISIBLE
            binding.llAppBarDashboard.llBack.visibility = View.GONE
        }
        binding.llAppBarDashboard.ivShare.visibility = View.GONE
        binding.llAppBarDashboard.llBack.setOnClickListener {
            onBackPressed()
        }
    }

    interface OnItemClickListener {
        fun onItemClick()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener =listener
    }

    fun changeShareIcon() {
        binding.llAppBarDashboard.rlBottom.visibility = View.GONE
        binding.llAppBarDashboard.ivLogo.visibility = View.GONE
        binding.llAppBarDashboard.ivAppName.visibility = View.GONE
        binding.llAppBarDashboard.ivDot.visibility = View.GONE
        binding.llAppBarDashboard.ivShare.visibility = View.VISIBLE
        binding.llAppBarDashboard.llBack.visibility = View.VISIBLE

        binding.llAppBarDashboard.llBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        utils.hideKeyboard(binding.llAppBarDashboard.ivDot)
        when {
            supportFragmentManager.backStackEntryCount > 1 -> {
                /* if(mFragment is HomeFragment){
                     (this as AppCompatActivity).supportActionBar?.show()
                     finish()
                 }else*/
                supportFragmentManager.popBackStack()
            }
            mFragment is HomeFragment -> {
                (this as AppCompatActivity).supportActionBar?.show()
                finish()
            }
            else -> {
                mFragment = HomeFragment()
                CommonUtils.setFragment(mFragment, true, this, R.id.frameContainer)
                (this as AppCompatActivity).supportActionBar?.show()
            }
        }
    }

    private fun logoutAlert() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle("Logout")
        builder.setMessage("Do you want to logout?")
        builder.setPositiveButton("Yes") { _, _ ->
            startActivity(Intent(this, LoginActivity::class.java))
            userPref.clearPref()
            finishAffinity()
        }

        builder.setNegativeButton("No", null)
        builder.create()
        builder.show()
    }
}