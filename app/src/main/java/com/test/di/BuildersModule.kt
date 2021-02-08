package com.test.di

import com.test.ui.activity.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Binds all sub-components within the app.
 */

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun MetricsActivity():MetricsActivity


}