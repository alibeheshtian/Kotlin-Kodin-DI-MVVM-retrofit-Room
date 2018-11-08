package com.iapptm.coreapp.di

import com.iapptm.coreapp.database.MovieDatabase
import com.iapptm.coreapp.database.dao.MovieDao
import com.iapptm.coreapp.repository.MovieRepository
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

private const val MODULE_NAME = "DataBase Module"

val dataBaseModel = Module(MODULE_NAME, false) {

    bind<MovieDatabase>() with singleton { MovieDatabase.getDatabase(instance()) }
    bind<MovieDao>() with provider { MovieDatabase.getDatabase(instance()).wordMovieDao() }
    bind<MovieRepository>() with provider { MovieRepository(instance()) }

}
