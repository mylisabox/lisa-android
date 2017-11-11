package com.mylisabox.lisa.profile

import android.content.Intent
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseNavigator
import com.mylisabox.lisa.common.MobileBaseActivity
import com.mylisabox.network.dagger.annotations.ActivityScope
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import javax.inject.Inject


@ActivityScope
class ProfileNavigator @Inject constructor(@ForActivity private val activity: MobileBaseActivity) : BaseNavigator(activity) {
    companion object {
        val REQUEST_PROFILE_PICTURE = 1
        val TYPE_IMAGE = "image/*"
    }

    fun goToPictureSelector() {
        val pickIntent = Intent(Intent.ACTION_GET_CONTENT)
        pickIntent.type = TYPE_IMAGE
        val pickIntent2 = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = TYPE_IMAGE

        //val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val pickTitle = activity.getString(R.string.pick_avatar)
        val chooserIntent = Intent.createChooser(pickIntent, pickTitle)
        chooserIntent.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                arrayOf(pickIntent2)
        )
        activity.startActivityForResult(chooserIntent, REQUEST_PROFILE_PICTURE)
    }
}