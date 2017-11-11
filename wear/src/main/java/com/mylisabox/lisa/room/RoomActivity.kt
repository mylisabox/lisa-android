package com.mylisabox.lisa.room

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.lisa.databinding.ActivityRoomBinding
import javax.inject.Inject

class RoomActivity : BaseActivity() {
    companion object {
        private val KEY_ROOM = "roomId"
        fun newInstance(context: Context, roomId: Long): Intent {
            val intent = Intent(context, RoomActivity::class.java)
            intent.putExtra(KEY_ROOM, roomId)
            return intent
        }
    }

    private val roomId by lazy {
        intent.getLongExtra(KEY_ROOM, 0)
    }

    @Inject lateinit var roomViewModel: RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityRoomBinding>(this, R.layout.activity_room)

        activityComponent.inject(this)

        binding.viewModel = getViewModel()

        // Enables Always-on
        setAmbientEnabled()
    }

    override fun getViewModel(): RoomViewModel {
        return roomViewModel
    }
}
