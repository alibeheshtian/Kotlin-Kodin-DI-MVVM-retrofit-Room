package com.iapptm.coreapp.di

import android.content.Context
import android.content.res.Resources
import android.preference.PreferenceManager
import com.iapptm.coreapp.CoreApplication
import com.iapptm.coreapp.data.services.IRxSchedulers
import com.iapptm.coreapp.data.services.PrefsUtils
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

private const val MODULE_NAME = "App Module"

val appModule = Module(MODULE_NAME, false) {
  bind<PrefsUtils>() with singleton { getPrefsUtils(instance("ApplicationContext")) }
  bind<Resources>() with singleton { instance<CoreApplication>().resources }
  bind<IRxSchedulers>() with singleton { getIRxSchedulers() }
}

private fun getPrefsUtils(context: Context): PrefsUtils = PrefsUtils(PreferenceManager.getDefaultSharedPreferences(context))

private fun getIRxSchedulers() = object : IRxSchedulers {
  override fun main(): Scheduler = AndroidSchedulers.mainThread()
  override fun io(): Scheduler = Schedulers.io()
}