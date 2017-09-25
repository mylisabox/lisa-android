package com.mylisabox.lisa.device.viewmodels

import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.FavoriteRepository
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.common.room.RoomRepository
import com.mylisabox.lisa.common.TemplateMobileViewBuilder
import com.mylisabox.network.utils.RxErrorForwarder
import javax.inject.Inject

class RoomViewModel @Inject constructor(roomRepository: RoomRepository,
                                        favoriteViewModel: FavoriteRepository,
                                        dashboardRepository: DashboardRepository,
                                        rxErrorForwarder: RxErrorForwarder,
                                        templateMobileViewBuilder: TemplateMobileViewBuilder,
                                        templateViewPopulator: TemplateViewPopulator) :
        DeviceViewModel(roomRepository, favoriteViewModel, dashboardRepository, rxErrorForwarder, templateMobileViewBuilder, templateViewPopulator)