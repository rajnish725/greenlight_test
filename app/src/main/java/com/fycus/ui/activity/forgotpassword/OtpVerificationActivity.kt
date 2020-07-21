package com.fycus.ui.activity.forgotpassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.fycus.BaseActivity
import com.fycus.R
import com.fycus.databinding.ActivityOtpVerificationBinding
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.*

class OtpVerificationActivity : BaseActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener, TextWatcher {
    private lateinit var binding: ActivityOtpVerificationBinding
    private var etDigitCurrent: EditText? = null
    private var otp = ""
    private var email = ""
    private var userId = ""
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verification)

        otp = this.intent.getStringExtra("otp")!!
        email = this.intent.getStringExtra("email")!!
        userId = this.intent.getStringExtra("user_id")!!
        type = this.intent.getStringExtra("type")!!
        if (type == "1") {
            utils.simpleAlert(this, "Your OTP", otp)
            binding.tvForgotType.text = "OTP has been sent to your phone number"
        } else if (type == "2") {
            utils.simpleAlert(this, "Your OTP", otp)
            binding.tvForgotType.text = "OTP has been sent to your E-mail ID"
        }

        initFocusListener()
        initTextChangeListener()
        initKeyListener()
        setListener()
    }

    private fun setListener() {
        binding.btnProceed.setOnClickListener(this)
        binding.tvResendOTP.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnProceed -> {
                if (isValid()) {
                    if (otp == makeOtp()) {
                        //Toast.makeText(this, "OTP Match", Toast.LENGTH_SHORT).show()
                         val intent = Intent(this,UpdatePasswordActivity::class.java)
                         intent.putExtra("user_id",userId)
                         startActivity(intent)
                         finish()
                    } else {
                        Toast.makeText(this, "OTP do not match", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.tvResendOTP -> {
                callResendOtpAPI()
            }
        }
    }

    private fun callResendOtpAPI() {
        val otp1 = (Random().nextInt(9999 - 1000 + 1) + 1000)

        apiService.callSendForgotOTPAPI(
            email,
            otp1.toString(),
            type
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    if (commonResponse.status == 1) {
                        otp = otp1.toString()
                        utils.simpleAlert(this, "Your OTP", otp)
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

    private fun initFocusListener() {
        binding.etDigit1.onFocusChangeListener = this
        binding.etDigit2.onFocusChangeListener = this
        binding.etDigit3.onFocusChangeListener = this
        binding.etDigit4.onFocusChangeListener = this

    }

    private fun initKeyListener() {
        binding.etDigit1.setOnKeyListener(this)
        binding.etDigit2.setOnKeyListener(this)
        binding.etDigit3.setOnKeyListener(this)
        binding.etDigit4.setOnKeyListener(this)
    }

    private fun initTextChangeListener() {
        binding.etDigit1.addTextChangedListener(this)
        binding.etDigit2.addTextChangedListener(this)
        binding.etDigit3.addTextChangedListener(this)
        binding.etDigit4.addTextChangedListener(this)
    }

    fun getOtp(): String? {
        return makeOtp()
    }

    fun setOtp(otp: String) {
        if (otp.length != 4) return
        binding.etDigit1.setText(otp[0].toString())
        binding.etDigit2.setText(otp[1].toString())
        binding.etDigit3.setText(otp[2].toString())
        binding.etDigit4.setText(otp[3].toString())
    }

    private fun makeOtp(): String? {
        val sb = StringBuilder()
        sb.append(binding.etDigit1.text.toString())
        sb.append(binding.etDigit2.text.toString())
        sb.append(binding.etDigit3.text.toString())
        sb.append(binding.etDigit4.text.toString())
        return sb.toString()
    }

    private fun isValid(): Boolean {
        return makeOtp()!!.length == 4
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (etDigitCurrent!!.text.isNotEmpty() && etDigitCurrent != binding.etDigit4) {
            if (count != 0) {
                etDigitCurrent!!.focusSearch(View.FOCUS_RIGHT).requestFocus()
                etDigitCurrent!!.imeOptions = EditorInfo.IME_ACTION_NEXT
            } else {
                etDigitCurrent!!.imeOptions = EditorInfo.IME_ACTION_DONE
                etDigitCurrent!!.focusSearch(View.FOCUS_LEFT).requestFocus()
            }

        } else if (etDigitCurrent!!.text.isNotEmpty() && etDigitCurrent == binding.etDigit4) {
            if (binding.etDigit1.text.isEmpty()) {
                binding.etDigit1.isFocusable = true
            }
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                binding.etDigit4.windowToken,
                0
            )
        } else {
            val currentValue = etDigitCurrent!!.text.toString()
            if (currentValue.isEmpty() && etDigitCurrent!!.selectionStart <= 0) {
                if (count != 0) {
                    etDigitCurrent!!.focusSearch(View.FOCUS_RIGHT).requestFocus()
                } else {
                    etDigitCurrent!!.focusSearch(View.FOCUS_LEFT).requestFocus()
                }
            }
        }
    }


    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN) {
            when (v!!.id) {

                R.id.etDigit1 -> {
                    if (binding.etDigit1.text.toString() != "")
                        return isKeyDel(binding.etDigit1, keyCode)
                }
                R.id.etDigit2 -> {
                    return isKeyDel(binding.etDigit2, keyCode)
                }
                R.id.etDigit3 -> {
                    return isKeyDel(binding.etDigit3, keyCode)
                }
                R.id.etDigit4 -> {
                    return isKeyDel(binding.etDigit4, keyCode)
                }
            }
        }
        return false
    }


    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        etDigitCurrent = view as EditText

        when (view.id) {
            R.id.etDigit1 -> {
                if (binding.etDigit1.text.toString() != "") {
                    etDigitCurrent!!.setSelection(etDigitCurrent!!.text.length)
                } else {
                }
            }
            /* R.id.etDigit4 -> {
                 if( binding.etDigit4.isFocused)
                 if(binding.etDigit4.text.isEmpty()){
                     binding.etDigit4.setBackgroundResource(R.drawable.round_otp_2)
                 }else{
                     binding.etDigit4.setBackgroundResource(R.drawable.round_otp_1)
                 }
             }*/
        }
    }

    private fun isKeyDel(etDigit: EditText, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            etDigit.text = null
            return true
        }
        return false
    }
}