package com.shenoto.domain.bases


import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.ToastUtils
import com.example.data.api.model.TokenModel
import com.shenoto.domain.extensions.DialogHelper
import com.shenoto.domain.interfaces.IRxSchedulers
import com.shenoto.domain.services.ApiService
import com.shenoto.domain.services.PrefsUtils
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel(private val appCompatActivity: AppCompatActivity) : ViewModel(), LifecycleObserver,
    KodeinAware {

    override val kodein: Kodein by kodein(appCompatActivity)
    override val kodeinContext = kcontext(appCompatActivity)

    /**
     * api service interface
     */
    protected val apiService: ApiService by instance()
    private val schedulers: IRxSchedulers by instance()

    private val prefsUtils: PrefsUtils by instance()
    private var compositeDisposable: CompositeDisposable? = null
    private var parentJob = Job()
    /**
     * loading liveData
     * return [Boolean] isLoading
     */
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * user is login or guest
     */
    val isLogin = prefsUtils.getAccessesToken() != null

    /**
     * scope for work by database in background thread
     */
    protected val scope: CoroutineScope by lazy { CoroutineScope(coroutineContext) }


    private val coroutineContext: CoroutineContext by lazy { parentJob + Dispatchers.Main }


    /**
     * when [ViewModel] is destroyed call this function to dispose and clear [CompositeDisposable]
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.let {
            it.dispose()
            it.clear()
            compositeDisposable = null
        }
    }

    private fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    /**
     * call service API function
     * @param singleApi = [ApiService]
     * @param onError = triggered when callService have exception
     * @param onSuccess = triggered when callService successful
     * @param onFinish = triggered when callService successful or exception state
     */
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

                        // handling exception
                        when (exception) {
                            // when trigger status code not 200 series
                            is HttpException -> onError?.let { onError(exception) }
                            // when trigger internet not respond
                            is UnknownHostException -> offlineDialog(singleApi, onSuccess, onFinish, onError)
                            // when trigger else
                            else -> ToastUtils.showLong("Something wrong!")
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