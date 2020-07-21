package com.fycus


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fycus.api.ApiService
import com.fycus.prefers.UserPref
import com.fycus.utils.Utils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


open class BaseFragment : Fragment() {

    @Inject
    lateinit var utils: Utils
    @Inject
    lateinit var userPref: UserPref
    @Inject
    lateinit var apiService: ApiService

    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

    }

    protected fun showProgressDialog() {
        if (dialog == null)
            dialog = Dialog(requireContext())
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        if (dialog != null && !dialog!!.isShowing)
            dialog!!.show()

    }

    protected fun hideProgressDialog() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
        /* if (progressDialog != null && progressDialog!!.isShowing)
             progressDialog!!.dismiss()*/
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }

}
