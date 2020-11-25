package com.yjpapp.stockportfolio.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.yjpapp.stockportfolio.R

class SplashActivity: BaseActivity(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startSplash()
    }

    private fun startSplash(){
        Handler().postDelayed({
            val intent = Intent(mContext, MainActivity::class.java)
            finish()
            startActivity(intent)
        },1500)
    }
}