package com.iapptm.coreapp.ui


import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.api.model.MovieModel
import com.example.data.database.entity.MovieEntity
import com.shenoto.domain.bases.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivityViewModel(app: AppCompatActivity) : BaseViewModel(app) {


    var movies: MutableLiveData<List<MovieModel>> = MutableLiveData()
    var moviesStored: LiveData<List<MovieEntity>> = repository.allMovies

    fun fetchMovies() {
        callService(
            singleApi = apiService.movies(),
            onSuccess = { movies.postValue(it) },
            onError = { Timber.e(it) }
        )
    }

    fun movieAddToFavorite(movieEntity: MovieEntity) = scope.launch(Dispatchers.IO) {
        repository.insertToDatabase(movieEntity)
    }


    class Factory(private val app: AppCompatActivity) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainActivityViewModel(app) as T
        }
    }
}