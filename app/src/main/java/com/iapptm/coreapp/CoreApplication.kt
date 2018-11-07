package com.iapptm.coreapp

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.Utils
import com.iapptm.coreapp.di.appModule
import com.iapptm.coreapp.di.networkModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class CoreApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        bind<Context>("ApplicationContext") with singleton { this@CoreApplication.applicationContext }
        bind<CoreApplication>() with singleton { this@CoreApplication }
        import(appModule)
        import(networkModule)
    }

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)

        // Set default font in over activities and fragment views from assets/fonts folder
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansMobile(FaNum).ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        if (BuildConfig.DEBUG) { Timber.plant(Timber.DebugTree()) }
    }
}