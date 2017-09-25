package com.mylisabox.lisa.device.viewmodels

import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.FavoriteRepository
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.lisa.common.TemplateMobileViewBuilder
import com.mylisabox.network.utils.RxErrorForwarder
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(favoriteRepository: FavoriteRepository,
                                            rxErrorForwarder: RxErrorForwarder,
                                            dashboardRepository: DashboardRepository,
                                            templateMobileViewBuilder: TemplateMobileViewBuilder,
                                            templateViewPopulator: TemplateViewPopulator) :
        DeviceViewModel(favoriteRepository, favoriteRepository, dashboardRepository, rxErrorForwarder, templateMobileViewBuilder, templateViewPopulator)