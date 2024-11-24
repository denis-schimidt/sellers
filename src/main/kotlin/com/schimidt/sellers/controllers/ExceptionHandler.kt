package com.schimidt.sellers.controllers

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.URI

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: MethodArgumentNotValidException): ProblemDetail {
        val detailFields = exception.bindingResult.fieldErrors.joinToString(separator = ", ") { fieldError -> fieldError.field }
        return ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = "Request Inválido"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/400")
            detail = "Os seguintes campos possuem dados inválidos: $detailFields"
            setProperty("violations", exception.bindingResult.fieldErrors.map { fieldError -> "${fieldError.field} -> ${fieldError.defaultMessage}" })
        }
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handle(exception: DataIntegrityViolationException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY)
        val detailFields = exception.rootCause ?: "Sql Error"
        problemDetail.title = "Invalid request"
        problemDetail.type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/422")
        problemDetail.detail = detailFields.toString()
        return problemDetail
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handle(exception: Exception): ProblemDetail {
        val problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        val detailFields = exception.cause ?: "Internal Server Error"
        problemDetail.title = "Internal Server Error"
        problemDetail.type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/500")
        problemDetail.detail = detailFields.toString()
        return problemDetail
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
