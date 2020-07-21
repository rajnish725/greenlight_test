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
import com.fycus.databinding.FragmentHelpBinding
import com.fycus.models.response.HelpModel
import com.fycus.ui.activity.HomeActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class HelpFragment : BaseFragment() {
    private lateinit var binding : FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_help, container, false)
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callGetHelpAPI()
    }

    private fun callGetHelpAPI() {
        apiService.callGetHelpAPI()
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

    private fun setData(mData: HelpModel?) {
        binding.tvMail.text = mData!!.mailus
        binding.tvCall.text = mData.contactus
        binding.tvReach.text = mData.reachus
    }
}