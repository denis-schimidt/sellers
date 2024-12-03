package com.schimidt.sellers.controllers.exceptions

import com.schimidt.sellers.domain.exceptions.CpfAlreadyExists
import com.schimidt.sellers.domain.exceptions.ResourceNotFoundException
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.net.URI

interface ProblemDetailFactory<T> {
    fun create(exception: T): ProblemDetail
}

@Component
class MethodArgumentNotValidProblemDetailFactory() : ProblemDetailFactory<MethodArgumentNotValidException> {

    override fun create(exception: MethodArgumentNotValidException): ProblemDetail {
        val detailFields = exception.bindingResult.fieldErrors.joinToString(separator = ", ") { fieldError -> fieldError.field }
        return ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = "Invalid Request"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/400")
            detail = "Os seguintes campos possuem dados inválidos: $detailFields"
            setProperty("violations", exception.bindingResult.fieldErrors.map { fieldError -> "${fieldError.field} -> ${fieldError.defaultMessage}" })
        }
    }
}

class CpfAlreadyExistsProblemDetailFactory() : ProblemDetailFactory<CpfAlreadyExists> {

    override fun create(exception: CpfAlreadyExists): ProblemDetail {
        return ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
            title = "Conflict"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/409")
            detail = exception.message
        }
    }
}

class DataIntegrityViolationProblemDetailFactory() : ProblemDetailFactory<DataIntegrityViolationException> {
    private val logger = org.slf4j.LoggerFactory.getLogger(DataIntegrityViolationProblemDetailFactory::class.java)

    override fun create(exception: DataIntegrityViolationException): ProblemDetail {
        logger.error("Data Integrity ViolationException -> ", exception.rootCause ?: exception)

        return ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
            title = "Conflict"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/409")
            detail = exception.rootCause?.message ?: "Conflict"
        }
    }
}

@Component
class HandlerMethodValidationProblemDetailFactory() : ProblemDetailFactory<Exception> {

    override fun create(exception: Exception): ProblemDetail {
        exception as HandlerMethodValidationException
        val violations = exception.valueResults.flatMap { valueResult ->
            valueResult.resolvableErrors.map { error ->
                "${error.codes?.firstOrNull()} -> ${error.defaultMessage}"
            }
        }

        return ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = "Invalid Request"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/400")
            detail = "Os seguintes campos possuem dados inválidos: ${violations.joinToString(", ")}"
            setProperty("violations", violations)
        }
    }
}

class ResourceNotFoundProblemDetailFactory() : ProblemDetailFactory<ResourceNotFoundException> {

    override fun create(exception: ResourceNotFoundException): ProblemDetail {
        return ProblemDetail.forStatus(HttpStatus.NOT_FOUND).apply {
            title = "Not Found"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/404")
            detail = exception.message
        }
    }
}

@Component
class NoResourceFoundProblemDetailFactory() : ProblemDetailFactory<NoResourceFoundException> {

    override fun create(exception: NoResourceFoundException): ProblemDetail {
        return ProblemDetail.forStatus(HttpStatus.NOT_FOUND).apply {
            title = "Not Found"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/404")
            detail = exception.message
        }
    }
}

class ConstraintViolationProblemDetailFactory() : ProblemDetailFactory<ConstraintViolationException> {
    private val logger = org.slf4j.LoggerFactory.getLogger(ConstraintViolationProblemDetailFactory::class.java)

    override fun create(exception: ConstraintViolationException): ProblemDetail {
        logger.error("Constraint Violation Exception -> ", exception.cause ?: exception)

        return ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
            title = "Conflict"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/409")
            detail = exception.cause?.message ?: exception.message
        }
    }
}

@Component
class ThrowableProblemDetailFactory() : ProblemDetailFactory<Throwable> {
    private val logger = org.slf4j.LoggerFactory.getLogger(ThrowableProblemDetailFactory::class.java)

    override fun create(exception: Throwable): ProblemDetail {
        logger.error("Throwable -> ", exception.cause ?: exception)

        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).apply {
            title = "Internal Server Error"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/500")
            detail = exception.cause?.message ?: exception.message
        }
    }
}

@Component
class ProblemDetailSelectorFactory {

    fun createProblemDetailBasedOn(exception: Throwable): ProblemDetail {
        return when (exception) {
            is MethodArgumentNotValidException -> MethodArgumentNotValidProblemDetailFactory().create(exception)
            is CpfAlreadyExists -> CpfAlreadyExistsProblemDetailFactory().create(exception)
            is ResourceNotFoundException -> ResourceNotFoundProblemDetailFactory().create(exception)
            is NoResourceFoundException -> NoResourceFoundProblemDetailFactory().create(exception)
            is ConstraintViolationException -> ConstraintViolationProblemDetailFactory().create(exception)
            is DataIntegrityViolationException -> DataIntegrityViolationProblemDetailFactory().create(exception)
            is HandlerMethodValidationException -> HandlerMethodValidationProblemDetailFactory().create(exception)
            else -> ThrowableProblemDetailFactory().create(exception)
        }
    }
}


