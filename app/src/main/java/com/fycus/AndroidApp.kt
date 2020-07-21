package com.fycus

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.fycus.di.AppComponent
import com.fycus.di.AppInjector
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class AndroidApp : Application(), HasActivityInjector, HasSupportFragmentInjector {

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }


    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>


    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        AppInjector.init(this)
        //    FacebookSdk.sdkInitialize(this)
        //Fresco.initialize(this)


    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }


    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

}