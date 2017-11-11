package com.mylisabox.lisa.device.viewmodels

import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.FavoriteRepository
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.common.room.RoomRepository
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.utils.RxErrorForwarder
import javax.inject.Inject

class RoomViewModel @Inject constructor(preferences: Preferences,
                                        roomRepository: RoomRepository,
                                        favoriteViewModel: FavoriteRepository,
                                        dashboardRepository: DashboardRepository,
                                        rxErrorForwarder: RxErrorForwarder,
                                        templateViewPopulator: TemplateViewPopulator) :
        DeviceViewModel(preferences, roomRepository, favoriteViewModel, dashboardRepository, rxErrorForwarder, templateViewPopulator)