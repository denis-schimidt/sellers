package com.schimidt.sellers.controllers.handlers

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.net.URI

@ControllerAdvice
class ExceptionHandler(
    private val methodArgumentNotValidFactory: MethodArgumentNotValidProblemDetailFactory,
    private val noResourceFoundFactory: NoResourceFoundProblemDetailFactory,
    private val throwableFactory: ThrowableProblemDetailFactory,
    private val handlerMethodValidationExceptionFactory: HandlerMethodValidationProblemDetailFactory,
    private val methodArgumentTypeMismatchExceptionProblemDetailFactory: MethodArgumentTypeMismatchExceptionProblemDetailFactory
) {
    private val log = LoggerFactory.getLogger(ExceptionHandler::class.java)

    @ExceptionHandler(HandlerMethodValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: HandlerMethodValidationException): ProblemDetail {
        return handlerMethodValidationExceptionFactory.create(exception)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: MethodArgumentNotValidException): ProblemDetail {
        return methodArgumentNotValidFactory.create(exception)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: MethodArgumentTypeMismatchException): ProblemDetail {
        return methodArgumentTypeMismatchExceptionProblemDetailFactory.create(exception)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: NoResourceFoundException): ProblemDetail {
        return noResourceFoundFactory.create(exception)
    }

    @ExceptionHandler(Throwable::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handle(exception: Throwable): ProblemDetail {
        log.error("An unexpected error occurred -> ", exception.cause ?: exception)
        return throwableFactory.create(exception)
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
