package com.iapptm.coreapp.ui


import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.example.data.api.model.MovieModel
import com.example.data.database.entity.MovieEntity
import com.shenoto.domain.bases.BaseViewModel
import com.shenoto.domain.repository.MovieRepository
import errorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance


class MainActivityViewModel(app: AppCompatActivity) : BaseViewModel(app) {
    private val repository: MovieRepository by instance()

    var movies: MutableLiveData<List<MovieModel>> = MutableLiveData()

    var moviesStored: LiveData<List<MovieEntity>> = repository.allMovies


    fun fetchMovies() {
        callService(
            singleApi = apiService.movies(),
            onSuccess = { movies.postValue(it) },
            onError = {
                // List<String> is Generic for mapping error respondBody
                ToastUtils.showLong(it.errorModel<List<String>>()?.message?.get(0))
            }
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