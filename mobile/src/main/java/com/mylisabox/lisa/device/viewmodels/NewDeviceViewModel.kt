package com.mylisabox.lisa.device.viewmodels

import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.FavoriteRepository
import com.mylisabox.common.device.NewDeviceRepository
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.lisa.common.TemplateMobileViewBuilder
import com.mylisabox.network.utils.RxErrorForwarder
import javax.inject.Inject

class NewDeviceViewModel @Inject constructor(deviceRepository: NewDeviceRepository,
                                             favoriteRepository: FavoriteRepository,
                                             dashboardRepository: DashboardRepository,
                                             rxErrorForwarder: RxErrorForwarder,
                                             templateMobileViewBuilder: TemplateMobileViewBuilder,
                                             templateViewPopulator: TemplateViewPopulator) :
        DeviceViewModel(deviceRepository, favoriteRepository, dashboardRepository, rxErrorForwarder, templateMobileViewBuilder, templateViewPopulator)