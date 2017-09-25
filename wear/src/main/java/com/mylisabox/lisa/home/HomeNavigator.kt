package com.mylisabox.lisa.home

import android.content.Context
import com.mylisabox.lisa.common.BaseNavigator
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import javax.inject.Inject

class HomeNavigator @Inject constructor(@ForActivity context: Context) : BaseNavigator(context) {

}