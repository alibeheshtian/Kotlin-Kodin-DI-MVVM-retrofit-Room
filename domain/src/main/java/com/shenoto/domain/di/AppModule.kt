package com.shenoto.domain.di

import com.shenoto.domain.interfaces.IRxSchedulers
import downloadSpeed
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

private const val MODULE_NAME = "App Module"

val appModule = Module(MODULE_NAME, false) {
    bind<IRxSchedulers>() with singleton { getIRxSchedulers() }
}


private fun getIRxSchedulers() = object : IRxSchedulers {
    override fun main(): Scheduler = AndroidSchedulers.mainThread()
    override fun io(): Scheduler = Schedulers.io()
}