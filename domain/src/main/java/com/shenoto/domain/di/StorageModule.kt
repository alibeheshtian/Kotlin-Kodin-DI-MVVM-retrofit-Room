package com.shenoto.domain.di

import android.content.Context
import android.preference.PreferenceManager
import com.shenoto.domain.services.PrefsUtils
import com.example.data.database.MovieDatabase
import com.example.data.database.dao.MovieDao
import com.shenoto.domain.repository.MovieRepository
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

private const val MODULE_NAME = "Storage Module"

val dataBaseModel = Module(MODULE_NAME, false) {

    bind<PrefsUtils>() with singleton { getPrefsUtils(instance()) }

    bind<MovieDatabase>() with singleton {MovieDatabase.getDatabase(instance("ApplicationContext") ) }
    bind<MovieDao>() with singleton { instance<MovieDatabase>().wordMovieDao() }
    bind<MovieRepository>() with provider { MovieRepository(instance()) }

}

private fun getPrefsUtils(context: Context): PrefsUtils =
    PrefsUtils(PreferenceManager.getDefaultSharedPreferences(context))
