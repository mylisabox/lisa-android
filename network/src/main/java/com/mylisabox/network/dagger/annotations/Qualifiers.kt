package com.mylisabox.network.dagger.annotations

import javax.inject.Qualifier

class Qualifiers {
    @Qualifier
    annotation class ForApplication

    @Qualifier
    annotation class ForActivity

    @Qualifier
    annotation class ForFragment

}