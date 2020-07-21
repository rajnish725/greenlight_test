package com.fycus.ui.activity.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fycus.BaseActivity
import com.fycus.R
import com.fycus.databinding.ActivityForgotPasswordBinding
import com.fycus.ui.activity.HomeActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.*

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    private var type = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
        setListener()
    }

    private fun setListener() {
        /* * type 0 for email and 1 for mobile number  **/
        binding.rgMain.setOnCheckedChangeListener { _, check ->
            if (check == R.id.rb_email) {
                binding.edtEmail.visibility = View.VISIBLE
                binding.edtMobile.visibility = View.GONE
                binding.btnProceed.visibility = View.VISIBLE
                type = 2
            } else if (check == R.id.rb_mobile) {
                binding.edtEmail.visibility = View.GONE
                binding.edtMobile.visibility = View.VISIBLE
                binding.btnProceed.visibility = View.VISIBLE
                type = 1
            }
        }

        binding.btnProceed.setOnClickListener {
            if(type == 2){
                if(binding.edtEmail.text.toString().isNotEmpty()){
                    callSendForgotOTPAPI(type,binding.edtEmail.text.toString())
                }else{
                    binding.edtEmail.error = "Please enter E-mail ID"
                    binding.edtEmail.requestFocus()
                }
            }else if(type == 1){
                if(binding.edtMobile.text.toString().isNotEmpty()){
                    if(binding.edtMobile.text.length == 10) {
                        callSendForgotOTPAPI(type, binding.edtMobile.text.toString())
                    }else{
                        binding.edtMobile.error = "Please enter valid Phone Number"
                        binding.edtMobile.requestFocus()
                    }
                }else{
                    binding.edtMobile.error = "Please enter Phone Number"
                    binding.edtMobile.requestFocus()
                }
            }
        }
    }

    private fun callSendForgotOTPAPI(type: Int, toString: String) {
        val otp = (Random().nextInt(9999 - 1000 + 1) + 1000)
        var forgotType = ""
        var email = ""
        if (type == 1) {
            forgotType = "mobile"
            email = binding.edtMobile.text.toString()
        } else {
            forgotType = "email"
            email = binding.edtEmail.text.toString()
        }
        apiService.callSendForgotOTPAPI(email,
            otp.toString(),
            type.toString()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    // utils.simpleAlert(this, "", it.msg.toString())
                    if (commonResponse.status == 1) {
                        val intent = Intent(this,OtpVerificationActivity::class.java)
                        intent.putExtra("type",type.toString())
                        intent.putExtra("otp",otp.toString())
                        intent.putExtra("email",email)
                        intent.putExtra("user_id",commonResponse.mData!!.user.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this,commonResponse.message, Toast.LENGTH_SHORT).show()
                    }
                }, {
                    hideProgressDialog()
                    if (it is ConnectException) {
                        utils.simpleAlert(
                            this,
                            getString(R.string.error),
                            getString(R.string.check_network_connection)
                        )
                    } else {
                        utils.simpleAlert(this, "", it.message.toString())
                    }
                }
            )

    }
}