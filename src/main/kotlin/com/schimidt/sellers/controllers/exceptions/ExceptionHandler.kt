package com.schimidt.sellers.controllers.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.URI

@ControllerAdvice
class ExceptionHandler(private val problemDetailSelectorFactory: ProblemDetailSelectorFactory) {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: MethodArgumentNotValidException): ProblemDetail {
        return problemDetailSelectorFactory.createProblemDetailBasedOn(exception)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handle(exception: Exception): ProblemDetail {
        return problemDetailSelectorFactory.createProblemDetailBasedOn(exception)
    }

    internal open class ProblemDetailCustom(
        val title: String,
        val type: URI,
        val detail: String,
        val status: Int,
        val instance: URI,
    )

    internal class ProblemDetailWithViolationsCustom(
        title: String, type: URI, detail: String, status: Int, instance: URI,
        val violations: List<String>? = null
    ) : ProblemDetailCustom(title = title, type = type, detail = detail, status = status, instance = instance)
}
