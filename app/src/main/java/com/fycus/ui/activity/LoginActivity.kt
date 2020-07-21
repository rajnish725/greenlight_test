package com.fycus.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fycus.BaseActivity
import com.fycus.R
import com.fycus.databinding.ActivityLoginBinding
import com.fycus.ui.activity.forgotpassword.ForgotPasswordActivity
import com.fycus.utils.CommonUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private var type = 1
    private var token: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setListener()
        getDeviceToken()
    }

    private fun setListener() {
        binding.tvForgot.setOnClickListener(this)
        binding.tvSignUp.setOnClickListener(this)
        binding.btnSignIn.setOnClickListener(this)
    }

    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result!!.token

                // Log and toast
                // val msg = getString(R.string.msg_token_fmt, token)
                Log.d("token", token!!)
            })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvForgot -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }
            R.id.tvSignUp -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.btnSignIn -> {
                if (checkValidation()) {
                    type = if (CommonUtils.isValidPhone(binding.edtEmail.text.toString())) {
                        1
                    } else {
                        2
                    }
                    callLoginAPI()
                }
            }
        }
    }

    /**
    loginid
    password
    'device_token'=>'required',
    'device_type'=>'required'
    type=1 for mobile and 2 for email
     */
    private fun callLoginAPI() {
        apiService.callLoginAPI(
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString(),
            token!!,
            "Android",
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
                        userPref.isLogin = true
                        userPref.user = commonResponse.mData!!
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        Toast.makeText(this, commonResponse.message, Toast.LENGTH_SHORT).show()
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

    private fun checkValidation(): Boolean {
        if (binding.edtEmail.text.toString().isEmpty()) {
            binding.edtEmail.error = "Please enter Phone/Email"
            binding.edtEmail.requestFocus()
            return false
        } else if (binding.edtPassword.text.toString().isEmpty()) {
            binding.edtPassword.error = "Please enter Password"
            binding.edtPassword.requestFocus()
            return false
        }
        return true

    }
}