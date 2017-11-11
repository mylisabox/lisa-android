package com.mylisabox.lisa.device.viewmodels

import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.FavoriteRepository
import com.mylisabox.common.device.NewDeviceRepository
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.utils.RxErrorForwarder
import javax.inject.Inject

class NewDeviceViewModel @Inject constructor(preferences: Preferences,
                                             deviceRepository: NewDeviceRepository,
                                             favoriteRepository: FavoriteRepository,
                                             dashboardRepository: DashboardRepository,
                                             rxErrorForwarder: RxErrorForwarder,
                                             templateViewPopulator: TemplateViewPopulator) :
        DeviceViewModel(preferences, deviceRepository, favoriteRepository, dashboardRepository, rxErrorForwarder, templateViewPopulator)