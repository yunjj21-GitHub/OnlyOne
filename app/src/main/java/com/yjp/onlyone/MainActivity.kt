package com.yjp.onlyone

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yjp.onlyone.base.BaseActivity
import com.yjp.onlyone.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    override fun createBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)
}