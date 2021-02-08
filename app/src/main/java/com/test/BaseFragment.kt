package com.test


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.test.api.ApiService
import com.test.utils.Utils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


open class BaseFragment : Fragment() {

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var apiService: ApiService

    var dialog1: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    protected fun showProgressDialog() {
        if (dialog1 == null)
            dialog1 = Dialog(requireContext())
      //  dialog1!!.setContentView(R.layout.progress_dialog)
        dialog1!!.setCancelable(false)
        dialog1!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        if (dialog1 != null && !dialog1!!.isShowing)
            dialog1!!.show()

    }

    protected fun hideProgressDialog() {
        if (dialog1!= null && dialog1!!.isShowing)
            dialog1!!.dismiss()
        /* if (progressDialog != null && progressDialog!!.isShowing)
             progressDialog!!.dismiss()*/
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog1 != null && dialog1!!.isShowing)
            dialog1!!.dismiss()
    }

}
