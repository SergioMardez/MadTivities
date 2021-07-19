package com.sergiom.madtivities.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergiom.madtivities.R
import com.sergiom.madtivities.ui.welcome.WelcomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WelcomeFragment.newInstance())
                .commitNow()
        }
    }
}