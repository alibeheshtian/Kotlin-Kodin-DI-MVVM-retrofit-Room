package com.shenoto.domain.bases

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.shenoto.app.customView.CustomLoading
import org.kodein.di.Copy
import org.kodein.di.android.kodein
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


abstract class BaseActivity(@LayoutRes val layout: Int) : AppCompatActivity(), KodeinAware {

    private val _parentKodein by kodein()
    private var customLoading: CustomLoading? = null


    override val kodein: Kodein by retainedKodein {
        extend(_parentKodein, copy = Copy.All)
        bind<Context>("ActivityContext") with singleton { this@BaseActivity }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        viewIsReady(savedInstanceState)
    }

    fun customLoading(viewGroup: ViewGroup? = null): CustomLoading {

        customLoading?.let {

        }
        if (customLoading == null) {
            customLoading = CustomLoading(this)
            customLoading?.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            if (viewGroup != null) {
                viewGroup.addView(customLoading)
            } else {
                rootViewGroup().addView(customLoading)
            }
        }
        return customLoading as CustomLoading

    }
    private fun rootViewGroup(): ViewGroup {
        return (this.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
    }

    abstract fun viewIsReady(savedInstanceState: Bundle?)

}