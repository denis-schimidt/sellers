package com.schimidt.sellers.controllers

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


    data class ProblemDetailCustom(
        var title: String? = null,
        var type: URI? = null,
        var detail: String? = null,
        var status: Int? = null,
        var instance: URI? = null,
        val violations: List<String>
    )
//
//    @ExceptionHandler(ConstraintViolationException::class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    fun handle(exception: ConstraintViolationException): ProblemDetail {
//        val problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
//        val detailFields = exception.constraintViolations.joinToString(", ") { violation -> "${violation.propertyPath}" }
//        problemDetail.title = "Invalid request"
//        problemDetail.type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/400")
//        problemDetail.detail = "Os seguintes campos possuem dados inválidos: $detailFields"
//        problemDetail.setProperty("violations", exception.constraintViolations.map { violation -> "${violation.propertyPath} -> ${violation.message}" })
//        return problemDetail
//    }
}
