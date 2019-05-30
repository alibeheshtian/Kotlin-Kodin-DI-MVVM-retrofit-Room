package com.iapptm.coreapp

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.Utils
import com.example.data.BuildConfig
import com.shenoto.domain.di.appModule
import com.shenoto.domain.di.dataBaseModel
import com.shenoto.domain.di.networkModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class CoreApplication : MultiDexApplication(), KodeinAware {

    override val kodein = Kodein.lazy {
        bind<Context>("ApplicationContext") with singleton { this@CoreApplication.applicationContext }
        bind<CoreApplication>() with singleton { this@CoreApplication }
        import(appModule)
        import(networkModule)
        import(dataBaseModel)
    }


    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
        MultiDex.install(this)
        // Set default font in over activities and fragment views from assets/fonts folder
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansMobile.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }


}