package com.fycus.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fycus.BaseActivity
import com.fycus.R

class SplashActivity : BaseActivity() {
    private  val splashTimeout :Long=3000 //3sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        Handler().postDelayed({
            //userPref.isLogin = true
            if(userPref.isLogin){
                startActivity(Intent(this, HomeActivity::class.java))
                // startActivity(Intent(this, LoginAndSignUpActivity::class.java))
                finish()
            }else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        },splashTimeout)
    }
}