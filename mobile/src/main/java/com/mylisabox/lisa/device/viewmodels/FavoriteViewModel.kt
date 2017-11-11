package com.mylisabox.lisa.device.viewmodels

import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.FavoriteRepository
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.utils.RxErrorForwarder
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(preferences: Preferences,
                                            favoriteRepository: FavoriteRepository,
                                            rxErrorForwarder: RxErrorForwarder,
                                            dashboardRepository: DashboardRepository,
                                            templateViewPopulator: TemplateViewPopulator) :
        DeviceViewModel(preferences, favoriteRepository, favoriteRepository, dashboardRepository, rxErrorForwarder, templateViewPopulator)