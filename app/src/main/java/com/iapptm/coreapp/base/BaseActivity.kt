package com.iapptm.coreapp.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.iapptm.coreapp.di.activityModule
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


abstract class BaseActivity(@LayoutRes val layout: Int) : AppCompatActivity(), KodeinAware {

    private val _parentKodein by closestKodein()


    override val kodein: Kodein by retainedKodein {
        extend(_parentKodein, copy = Copy.All)
        bind<Activity>() with singleton { this@BaseActivity }
        bind<Context>("ActivityContext") with singleton { this@BaseActivity }
        import(activityModule)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        viewIsReady(savedInstanceState)
    }


    abstract fun viewIsReady(savedInstanceState: Bundle?)

}