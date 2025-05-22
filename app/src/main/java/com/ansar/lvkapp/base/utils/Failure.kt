package com.ansar.lvkapp.base.utils

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [Failure] class.
 */
sealed class Failure(override val message: String) : Throwable() {
    class Message(message: String) : Failure(message)
    class UseCase(val e: Exception) : Failure(e.message.orEmpty())
    object InternetConnection : Failure("Нет подключения к интернету")
    object NotFound : Failure("")
    object AuthTokenFailure : Failure("")
    object StatusChange : Failure("")
    object Status : Failure("")
    object NotAuth : Failure("Вы не авторизованы")
}