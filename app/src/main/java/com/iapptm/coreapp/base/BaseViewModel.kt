package com.iapptm.coreapp.base


import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.iapptm.coreapp.data.services.ApiService
import com.iapptm.coreapp.data.services.IRxSchedulers
import com.iapptm.coreapp.repository.MovieRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import timber.log.Timber

abstract class BaseViewModel(context: Context) : ViewModel(), LifecycleObserver , KodeinAware {
  override val kodein: Kodein by closestKodein(context)
  override val kodeinContext = kcontext(context)
  private var compositeDisposable: CompositeDisposable? = null
  protected val apiService: ApiService by instance()
  protected val schedulers: IRxSchedulers by instance()
  protected val repository: MovieRepository by instance()
  protected val ctx =context

  override fun onCleared() {
    super.onCleared()
    Timber.d("unsubscribeFromDataStore(): ")
    if (compositeDisposable != null) {
      compositeDisposable!!.dispose()
      compositeDisposable!!.clear()
      compositeDisposable = null
    }
  }

  protected fun addDisposable(disposable: Disposable) {
    if (compositeDisposable == null) {
      compositeDisposable = CompositeDisposable()
    }
    compositeDisposable!!.add(disposable)
  }

}