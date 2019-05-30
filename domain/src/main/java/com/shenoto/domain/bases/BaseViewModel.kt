package com.shenoto.domain.bases


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.api.model.TokenModel
import com.shenoto.domain.extensions.DialogHelper
import com.shenoto.domain.interfaces.IRxSchedulers
import com.shenoto.domain.services.ApiService
import com.shenoto.domain.repository.MovieRepository
import com.shenoto.domain.services.PrefsUtils
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.kodein.di.Kodein
import org.kodein.di.android.kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(protected val appCompatActivity: AppCompatActivity) : ViewModel(), LifecycleObserver , KodeinAware {
  override val kodein: Kodein by kodein(appCompatActivity)
  override val kodeinContext = kcontext(appCompatActivity)
  private var compositeDisposable: CompositeDisposable? = null
  protected val apiService: ApiService by instance()
  protected val schedulers: IRxSchedulers by instance()
  private var parentJob = Job()
  private val coroutineContext: CoroutineContext = parentJob + Dispatchers.Main
  protected val prefsUtils: PrefsUtils by instance()
  val isLogin = prefsUtils.getAccessesToken() != null
  protected val scope = CoroutineScope(coroutineContext)
  val loading: MutableLiveData<Boolean> = MutableLiveData()


  protected val repository: MovieRepository by instance()



  override fun onCleared() {
    super.onCleared()
    compositeDisposable?.let {
      it.dispose()
      it.clear()
      compositeDisposable = null
    }
  }

  protected fun addDisposable(disposable: Disposable) {
    if (compositeDisposable == null) {
      compositeDisposable = CompositeDisposable()
    }
    compositeDisposable!!.add(disposable)
  }

  protected fun <T> callService(
    singleApi: Single<T>,
    onSuccess: (T) -> Unit,
    onFinish: (() -> Unit)? = null,
    onError: ((HttpException) -> Unit)? = null
  ) {
    loading.postValue(true)
    addDisposable(
      singleApi.subscribeOn(schedulers.io())
        .observeOn(schedulers.main())
        .doFinally {
          loading.postValue(false)
          onFinish?.let { onFinish() }
        }
        .subscribe(
          {
            onSuccess(it)
          },
          { exception ->
            when (exception) {
              is HttpException -> onError?.let {

                onError(exception)

              }
              is UnknownHostException -> offlineDialog(singleApi, onSuccess, onFinish, onError)
            }
          })
    )
  }

  private fun <T> offlineDialog(
    singleApi: Single<T>,
    onSuccess: (T) -> Unit,
    onFinish: (() -> Unit)? = null,
    onError: ((HttpException) -> Unit)? = null
  ) {

    val builder = AlertDialog.Builder(appCompatActivity)

    builder.setCancelable(false).setMessage("Check you internet")
      .setNegativeButton("retry") { _, _ -> callService(singleApi, onSuccess, onFinish, onError) }


  }

  open fun logout() {
    DialogHelper.showLogoutDialog {
      prefsUtils.logout()
      appCompatActivity.finishAffinity()
    }
  }

  open fun login(token: TokenModel) {
    prefsUtils.setAccessesToken(token.accessToken)
    prefsUtils.setRefreshToken(token.refreshToken)
  }

}