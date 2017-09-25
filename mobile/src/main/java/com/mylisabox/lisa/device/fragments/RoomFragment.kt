package com.mylisabox.lisa.device.fragments

import android.os.Bundle
import com.mylisabox.lisa.device.viewmodels.RoomViewModel
import javax.inject.Inject


/**
 * Created by jaumard on 07.12.16.
 * L.I.S.A. project license GPL-3
 */

class RoomFragment : DevicesFragment() {
    companion object {
        private val ROOM_ID = "roomId"
        private val ROOM_NAME = "roomName"

        fun newInstance(roomId: Long, roomName: String): RoomFragment {
            val roomFragment = RoomFragment()
            val bundle = Bundle()
            bundle.putLong(ROOM_ID, roomId)
            bundle.putString(ROOM_NAME, roomName)
            roomFragment.arguments = bundle
            return roomFragment
        }
    }

    @Inject
    lateinit var roomViewModel: RoomViewModel

    override fun onStart() {
        super.onStart()
        activity?.title = arguments?.getString(ROOM_NAME)
    }

    override fun inject() {
        getFragmentComponent(this).inject(this)
        roomViewModel.roomId = arguments?.getLong(ROOM_ID) ?: 0
        vModel = roomViewModel
        super.inject()
    }
}