package com.mylisabox.lisa.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.MobileBaseActivity
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : MobileBaseActivity() {
    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isFinishing) {
            setContentView(R.layout.fragment_container)
            integrateToolbar()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            replaceFragment(ProfileFragment.newInstance(), false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ProfileNavigator.REQUEST_PROFILE_PICTURE && resultCode == Activity.RESULT_OK) {
            val stream = this.contentResolver.openInputStream(data?.data)
            val file = File(cacheDir, "avatar.img")
            val outputStream = FileOutputStream(file)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int = stream.read(buffer)

                while (read != -1) {
                    output.write(buffer, 0, read)
                    read = stream.read(buffer)
                }
                output.flush()
            }

            (getCurrentFragment() as ProfileFragment).vModel.avatar.set(file)
        }
    }
}
