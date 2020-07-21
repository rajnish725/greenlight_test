package com.fycus

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fycus.api.ApiService
import com.fycus.prefers.UserPref
import com.fycus.utils.Utils
import dagger.android.AndroidInjection
import javax.inject.Inject

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var utils: Utils
    @Inject
    lateinit var userPref: UserPref
    @Inject
    lateinit var apiService: ApiService

    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    protected fun showProgressDialog() {
        if (dialog == null)
            dialog = Dialog(this)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)


        if (dialog != null && !dialog!!.isShowing)
            dialog!!.show()
        /* if (progressDialog == null)
             progressDialog = ProgressDialog(this, R.style.Theme_AppCompat_DayNight_Dialog_MinWidth)
         progressDialog!!.setMessage("Please wait...")
         progressDialog!!.setCancelable(false)

         if (progressDialog != null && !progressDialog!!.isShowing)
             progressDialog!!.show()*/
    }

    protected fun hideProgressDialog() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()

    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }
}
