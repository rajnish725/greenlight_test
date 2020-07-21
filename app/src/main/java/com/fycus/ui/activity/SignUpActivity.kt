package com.fycus.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fycus.BaseActivity
import com.fycus.R
import com.fycus.databinding.ActivitySignUpBinding
import com.fycus.utils.CommonUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class SignUpActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySignUpBinding
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        setListener()
        getDeviceToken()
    }

    private fun setListener() {
        binding.tvLogin.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
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
            R.id.tvLogin -> {
                finish()
            }
            R.id.btnSignUp -> {
                if (checkValidation()) {
                    callSignUpAPI()
                }
            }
        }
    }

    /**
     * name:mayank
    email:test@gmail.com
    mobile:88580926844
    password:123
    c_password:123
    'device_token'=>'required',
    'device_type'=>'required'
     */
    private fun callSignUpAPI() {
        apiService.callSignUpAPI(binding.edtName.text.toString(),
            binding.edtEmail.text.toString(),
            binding.edtMobileNumber.text.toString(),
            binding.edtPassword.text.toString(),
            binding.edtConfirmPassword.text.toString(),
            token,
            "Android"
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    // utils.simpleAlert(this, "", it.msg.toString())
                    if (commonResponse.status == 1) {
                        Toast.makeText(this,commonResponse.message, Toast.LENGTH_SHORT).show()
                        userPref.isLogin = true
                        userPref.user = commonResponse.mData!!
                        startActivity(Intent(this,HomeActivity::class.java))
                        finishAffinity()
                    }else{
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

    private fun checkValidation(): Boolean {
        when {
            binding.edtName.text.toString().isEmpty() -> {
                binding.edtName.error = "Please enter Name"
                binding.edtName.requestFocus()
                return false
            }
            binding.edtEmail.text.toString().isEmpty() -> {
                binding.edtEmail.error = "Please enter E-mail ID"
                binding.edtEmail.requestFocus()
                return false
            }
            !CommonUtils.isValidEmail(binding.edtEmail.text.toString()) -> {
                binding.edtEmail.error = "Please enter valid E-mail ID"
                binding.edtEmail.requestFocus()
                return false
            }
            binding.edtMobileNumber.text.toString().isEmpty() -> {
                binding.edtMobileNumber.error = "Please enter Mobile Number"
                binding.edtMobileNumber.requestFocus()
                return false
            }
            binding.edtMobileNumber.text.toString().length != 10 -> {
                binding.edtMobileNumber.error = "Please enter valid Mobile Number"
                binding.edtMobileNumber.requestFocus()
                return false
            }
            binding.edtPassword.text.toString().isEmpty() -> {
                binding.edtPassword.error = "Please enter Password"
                binding.edtPassword.requestFocus()
                return false
            }
            binding.edtConfirmPassword.text.toString().isEmpty() -> {
                binding.edtConfirmPassword.error = "Please enter confirm Password"
                binding.edtConfirmPassword.requestFocus()
                return false
            }
            binding.edtConfirmPassword.text.toString() != binding.edtPassword.text.toString() -> {
                binding.edtConfirmPassword.error = "Confirm Password do not match"
                binding.edtConfirmPassword.requestFocus()
                return false
            }
            else -> return true
        }
    }
}
