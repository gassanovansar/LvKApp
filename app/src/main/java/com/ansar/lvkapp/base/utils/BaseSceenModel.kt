package com.ansar.lvkapp.base.utils

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitInternal
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.SimpleContext
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax


abstract class BaseScreenModel<State : Any, SideEffect : Any>(initState: State) :
    ScreenModel, ContainerHost<State, SideEffect>, KoinComponent {

//    private val notificationManager: NotificationManager by inject()

    private val defaultModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    final override val container: Container<State, SideEffect> =
        screenModelScope.container(initState)

    @OptIn(OrbitInternal::class)
    protected fun <S : Any, SE : Any> SimpleSyntax<S, SE>.reduceLocal(reducer: SimpleContext<S>.() -> S) {
        screenModelScope.launch {
            containerContext.reduce { reducerState ->
                SimpleContext(reducerState).reducer()
            }
        }
    }

    @OptIn(OrbitInternal::class)
    protected fun <S : Any, SE : Any> SimpleSyntax<S, SE>.postSideEffectLocal(sideEffect: SE) {
        screenModelScope.launch {
            containerContext.postSideEffect(sideEffect)
        }
    }


    protected val state: State
        get() = container.stateFlow.value

    val stateFlow = container.stateFlow

    /**
     * Выполнить запросы
     * @param operation - описываем запрос
     * @param loading - статус загрузки(можем переопределить)
     * @param failure - подписка на ошибку(можем переопределить)
     * @param success - результат
     */
//    protected fun <T> launchOperation(
//        operation: suspend (CoroutineScope) -> Either<Failure, T>,
//        loading: (Boolean) -> Unit = { setStatus(it) },
//        failure: (Failure) -> Unit = { handleError(it) },
//        success: (T) -> Unit = {},
//    ): Job {
//        return screenModelScope.launch(handler) {
//            loading.invoke(true)
//            withContext(defaultModelScope.coroutineContext) {
//                operation(this)
//            }.fold(failure, success)
//            loading.invoke(false)
//        }
//    }

//    protected val handler = CoroutineExceptionHandler { _, exception ->
//        println(exception)
//        if (exception.message?.contains(
//                "HttpClientCall",
//                true
//            ) == true
//        ) return@CoroutineExceptionHandler
//        if (exception.message?.contains("host", true) == true) {
//            handleError(Failure.InternetConnection)
//        } else if (exception.message?.contains("1009", true) == true) {
//            handleError(Failure.InternetConnection)
//        } else if (exception.message?.contains("Вы не авторизованы", true) == true) {
//            handleError(Failure.NotAuth)
//        } else {
//            handleError(exception)
//        }
//    }

//    protected fun handleError(failure: Throwable) {
//        println(failure)
//        val message = failure.message ?: "Неизвестна ошибка"
//        if (message.contains("Вы не авторизованы", true)) {
//            setError(Failure.NotAuth)
//        } else if (!message.contains("Parent job is Completed", false)) setError(message)
//    }


    /**
     * Ошибки наследованные от Failure
     */
//    private val _error = Channel<Notification?>(Channel.BUFFERED)
//    val error = _error.receiveAsFlow().filterNotNull()
//

    /**
     * Статус загрузки(launchOperation переключает)
     */
    private val _status = MutableStateFlow(false)
    val status = _status


    protected fun setStatus(status: Boolean) {
        screenModelScope.launch {
            _status.emit(status)
        }
    }

    /**
     * Показать ошибку
     */
//    fun setError(error: String) {
//        screenModelScope.launch {
//            _status.emit(false)
//            _error.send(Notification.Failure(message = error))
//            delay(100)
//            _error.send(null)
//        }
//    }

//    private fun setError(failure: Failure) {
//        screenModelScope.launch {
//            _status.emit(false)
//            _error.send(Notification.Failure(message = failure.message))
//            delay(100)
//            _error.send(null)
//        }
//    }

//    protected fun setSuccess(message: String) {
//        screenModelScope.launch {
//            _status.emit(false)
//            _error.send(Notification.Success(message = message))
//            delay(100)
//            _error.send(null)
//        }
//    }

//    protected fun showGlobalMessage(notification: Notification) {
//        screenModelScope.launch {
//            notificationManager.notification(notification)
//        }
//    }
}
