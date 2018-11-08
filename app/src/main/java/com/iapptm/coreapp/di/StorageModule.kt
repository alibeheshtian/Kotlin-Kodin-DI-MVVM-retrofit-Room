package com.iapptm.coreapp.di

import android.content.Context
import android.preference.PreferenceManager
import com.iapptm.coreapp.data.services.PrefsUtils
import com.iapptm.coreapp.database.MovieDatabase
import com.iapptm.coreapp.database.dao.MovieDao
import com.iapptm.coreapp.repository.MovieRepository
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

private const val MODULE_NAME = "Storage Module"

val dataBaseModel = Module(MODULE_NAME, false) {

    bind<PrefsUtils>() with singleton { getPrefsUtils(instance()) }

    bind<MovieDatabase>() with singleton { MovieDatabase.getDatabase(instance("ApplicationContext") ) }
    bind<MovieDao>() with singleton { instance<MovieDatabase>().wordMovieDao() }
    bind<MovieRepository>() with provider { MovieRepository(instance()) }

}

private fun getPrefsUtils(context: Context): PrefsUtils =
    PrefsUtils(PreferenceManager.getDefaultSharedPreferences(context))
