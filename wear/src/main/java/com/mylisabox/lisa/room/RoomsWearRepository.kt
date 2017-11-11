package com.mylisabox.lisa.room

import com.mylisabox.common.CommonApplication.Companion.KEY_ROOM
import com.mylisabox.common.CommonApplication.Companion.WEAR_ROOMS_PATH
import com.mylisabox.common.wearable.Wearable
import com.mylisabox.network.room.models.Room
import io.reactivex.Single
import javax.inject.Inject

class RoomsWearRepository @Inject constructor(private val wearable: Wearable) {
    fun retrieveAll(): Single<List<Room>> {
        return wearable.retrieveData(Array<Room>::class.java, WEAR_ROOMS_PATH)
                .map { it.toList() }
                .doOnError {
                    wearable.sendMessage(WEAR_ROOMS_PATH, KEY_ROOM).subscribe()
                }
                .retry(2)
    }

}