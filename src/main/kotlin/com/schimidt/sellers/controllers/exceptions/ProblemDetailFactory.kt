package com.schimidt.sellers.controllers.exceptions

import com.schimidt.sellers.domain.exceptions.CpfCnpjAlreadyExists
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import java.net.URI

interface ProblemDetailFactory<T> {
    fun create(exception: T): ProblemDetail
}

class MethodArgumentNotValidExceptionFactory() : ProblemDetailFactory<MethodArgumentNotValidException> {

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

class CpfCnpjAlreadyExistsFactory() : ProblemDetailFactory<CpfCnpjAlreadyExists> {

    override fun create(exception: CpfCnpjAlreadyExists): ProblemDetail {
        return ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
            title = "Conflict"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/409")
            detail = exception.message
        }
    }
}

class DataIntegrityViolationExceptionFactory() : ProblemDetailFactory<DataIntegrityViolationException> {
    private val logger = org.slf4j.LoggerFactory.getLogger(DataIntegrityViolationExceptionFactory::class.java)

    override fun create(exception: DataIntegrityViolationException): ProblemDetail {
        logger.error("Data Integrity ViolationException -> ", exception.rootCause ?: exception)

        return ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
            title = "Conflict"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/409")
            detail = exception.rootCause?.message ?: "Conflict"
        }
    }
}

class ConstraintViolationExceptionFactory() : ProblemDetailFactory<ConstraintViolationException> {
    private val logger = org.slf4j.LoggerFactory.getLogger(ConstraintViolationExceptionFactory::class.java)

    override fun create(exception: ConstraintViolationException): ProblemDetail {
        logger.error("Constraint Violation Exception -> ", exception.cause ?: exception)

        return ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
            title = "Conflict"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/409")
            detail = exception.cause?.message ?: exception.message
        }
    }
}

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
            is MethodArgumentNotValidException -> MethodArgumentNotValidExceptionFactory().create(exception)
            is CpfCnpjAlreadyExists -> CpfCnpjAlreadyExistsFactory().create(exception)
            is ConstraintViolationException -> ConstraintViolationExceptionFactory().create(exception)
            is DataIntegrityViolationException -> DataIntegrityViolationExceptionFactory().create(exception)
            else -> ThrowableProblemDetailFactory().create(exception)
        }
    }
}



