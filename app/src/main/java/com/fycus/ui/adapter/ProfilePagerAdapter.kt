package com.fycus.ui.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.fycus.R
import com.fycus.databinding.ProfileCardPagerBinding

class ProfilePagerAdapter(
    val context: Context?
) : PagerAdapter() {
    private var listener: OnItemClickListener? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val binding: ProfileCardPagerBinding =
            DataBindingUtil.inflate(inflater, R.layout.profile_card_pager, view, false)!!

        if(position == 0){
            binding.llFirst.visibility = View.VISIBLE
            binding.llMain.visibility = View.GONE
        }else {
            binding.llFirst.visibility = View.GONE
            binding.llMain.visibility = View.VISIBLE
        }

        binding.cardView.setOnClickListener {
            if(listener != null){
                listener!!.onItemClick(position)
            }
        }

        view.addView(binding.root, 0)

        return binding.root
    }

    override fun getCount(): Int {
        return 3
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // super.destroyItem(container, position, `object`)
    }

    override fun saveState(): Parcelable? {
        return null
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}