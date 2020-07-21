package com.fycus.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentFaqBinding
import com.fycus.models.FaqModel
import com.fycus.models.response.FaqMainModel
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.FaqExpandableAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class FaqFragment : BaseFragment() {
    private lateinit var binding : FragmentFaqBinding
    private var expandableAdapter: FaqExpandableAdapter? = null
    private var headerList: ArrayList<String>? = null
    private var childList: ArrayList<FaqModel>? = null
    private var lastExpandedPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_faq, container, false)
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerList = ArrayList()
        childList = ArrayList()
        callGetFaqAPI()
    }

    private fun callGetFaqAPI() {
        apiService.callGetFaqAPI()
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

    private fun setData(mData: List<FaqMainModel>?) {
        headerList!!.clear()
        childList!!.clear()
        for(i in mData!!.indices){
            headerList!!.add(mData[i].title)
            childList!!.add(FaqModel(mData[i].description))
        }

      /*  headerList!!.add("How can I change my password?")
        headerList!!.add("Can I change my E-mail ID?")

        childList!!.add(FaqModel(getString(R.string.loren)))
        childList!!.add(FaqModel(getString(R.string.loren)))*/
        expandableAdapter = FaqExpandableAdapter(requireContext(), childList!!, headerList!!)
        binding.expandedMenu.setAdapter(expandableAdapter)

        binding.expandedMenu.setOnGroupExpandListener {

            if (lastExpandedPosition != -1 && it != lastExpandedPosition) {
                binding.expandedMenu.collapseGroup(lastExpandedPosition)
            }
            lastExpandedPosition = it
        }
        expandableAdapter!!.notifyDataSetChanged()
    }
}