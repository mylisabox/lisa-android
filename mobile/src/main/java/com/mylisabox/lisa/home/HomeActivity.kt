package com.mylisabox.lisa.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.view.Gravity
import android.view.View
import com.mylisabox.common.CommonApplication
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.MobileBaseActivity
import com.mylisabox.lisa.databinding.HomeActivityBinding
import com.mylisabox.lisa.device.fragments.FavoritesFragment
import com.mylisabox.lisa.device.fragments.RoomFragment
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import com.mylisabox.network.preferences.Preferences
import javax.inject.Inject

class HomeActivity : MobileBaseActivity() {
    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }

    @Inject
    @ForActivity lateinit var mainMenuViewModel: MainMenuViewModel
    @Inject lateinit var preferences: Preferences
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var binding: HomeActivityBinding
    private lateinit var drawerLayout: DrawerLayout
    private var isTablet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isFinishing) {
            binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
            drawerLayout = findViewById(R.id.drawer_layout)
            integrateToolbar()

            isTablet = resources.getBoolean(R.bool.isTablet)

            drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
            if (isTablet) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.setDisplayShowHomeEnabled(false)
                supportActionBar?.setHomeButtonEnabled(false)
                supportActionBar?.setIcon(ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)))
            } else {
                drawerLayout.addDrawerListener(drawerToggle)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setHomeButtonEnabled(true)
            }

            if (intent.flags == Intent.FLAG_ACTIVITY_CLEAR_TASK) {
                drawerLayout.openDrawer(Gravity.START, false)
            }

            if (isTablet) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
                drawerLayout.setScrimColor(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) resources.getColor(android.R.color.transparent, theme)
                        else resources.getColor(android.R.color.transparent)
                )
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            activityComponent.inject(this)
            init()
            val lastRoomId = preferences.get<Long?>(CommonApplication.KEY_ROOM, null)
            val lastRoomName: String = preferences.get(CommonApplication.KEY_ROOM_NAME, "")!!

            clearHistory() // in case of screen rotation
            if (lastRoomId == null || lastRoomId == 0L) {
                replaceFragment(FavoritesFragment.newInstance(), false)
            } else {
                replaceFragment(RoomFragment.newInstance(lastRoomId, lastRoomName), false)
            }
        }
    }

    private fun clearHistory() {
        val fm = supportFragmentManager
        val count = fm.backStackEntryCount
        for (i in 0 until count) {
            fm.popBackStackImmediate()
        }
    }

    override fun onResume() {
        super.onResume()
        mainMenuViewModel.bind()
    }

    override fun onPause() {
        super.onPause()
        mainMenuViewModel.unbind()
    }

    private fun init() {
        val theme: Int? = appPrefs.get(CommonApplication.KEY_THEME)
        mainMenuViewModel.isNightTheme.set(theme == AppCompatDelegate.MODE_NIGHT_YES)
        binding.viewModel = mainMenuViewModel
    }

    fun closeDrawer() {
        if (!isTablet) {
            drawerLayout.closeDrawer(Gravity.START)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START) && !isTablet) {
            closeDrawer()
        } else if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (!isTablet) {
            drawerToggle.syncState()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (!isTablet) {
            drawerToggle.onConfigurationChanged(newConfig)
        }
    }

    fun goToSpeech(v: View) {
        goToSpeech()
    }
}
