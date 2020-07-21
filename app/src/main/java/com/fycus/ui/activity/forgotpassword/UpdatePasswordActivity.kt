package com.fycus.ui.activity.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fycus.BaseActivity
import com.fycus.R
import com.fycus.databinding.ActivityUpdatePasswordBinding
import com.fycus.ui.activity.LoginActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class UpdatePasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityUpdatePasswordBinding
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_password)
        setListener()
        userId = this.intent.getStringExtra("user_id")!!
    }

    private fun setListener() {
        binding.btnUpdatePassword.setOnClickListener {
            if (checkValidation()) {
                //Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()
                callPasswordUpdateAPI()
            }
        }
    }

    private fun callPasswordUpdateAPI() {
        apiService.callPasswordUpdateAPI(userId,
            binding.edtPassword.text.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    // utils.simpleAlert(this, "", it.msg.toString())
                    if (commonResponse.status == 1) {
                        val builder = androidx.appcompat.app.AlertDialog.Builder(this, R.style.AlertDialogTheme)
                        builder.setMessage("Password updated successfully")
                        builder.setPositiveButton("Yes") { _, _ ->
                            startActivity(Intent(this, LoginActivity::class.java))
                            finishAffinity()
                        }
                        builder.setCancelable(false)
                        builder.create()
                        builder.show()
                        //Toast.makeText(this,commonResponse.message, Toast.LENGTH_SHORT).show()
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
        return when {
            binding.edtPassword.text.toString().isEmpty() -> {
                binding.edtPassword.error = "Please enter Password"
                binding.edtPassword.requestFocus()
                false
            }
            binding.edtConfirmPassword.text.toString().isEmpty() -> {
                binding.edtConfirmPassword.error = "Please enter confirm Password"
                binding.edtConfirmPassword.requestFocus()
                false
            }
            binding.edtConfirmPassword.text.toString() != binding.edtPassword.text.toString() -> {
                binding.edtConfirmPassword.error = "Confirm Password do not match"
                binding.edtConfirmPassword.requestFocus()
                false
            }
            else -> true
        }

    }
}
