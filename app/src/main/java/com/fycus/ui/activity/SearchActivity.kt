package com.fycus.ui.activity

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.fycus.BaseActivity
import com.fycus.R
import com.fycus.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity() {
    private lateinit var binding : ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        //setSupportActionBar(binding.toolbar)
        /*supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        //supportActionBar?.title = "Details"
        //binding.toolbar.navigationIcon = this.getDrawable(R.drawable.back)
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = getColor(R.color.colorPrimaryDark)
            } else {
                window.statusBarColor = Color.BLUE
            }
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}