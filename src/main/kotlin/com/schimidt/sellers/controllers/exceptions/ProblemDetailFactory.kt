package com.schimidt.sellers.controllers.exceptions

import com.schimidt.sellers.domain.exceptions.CpfAlreadyExists
import com.schimidt.sellers.domain.exceptions.ResourceNotFoundException
import org.hibernate.exception.ConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.net.URI

interface ProblemDetailFactory<T : Throwable> {
    fun create(exception: T): ProblemDetail
    fun getThrowableType(): Class<T>
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

    override fun getThrowableType(): Class<MethodArgumentNotValidException> {
        return MethodArgumentNotValidException::class.java
    }
}

@Component
class CpfAlreadyExistsProblemDetailFactory() : ProblemDetailFactory<CpfAlreadyExists> {

    override fun create(exception: CpfAlreadyExists): ProblemDetail {
        return ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
            title = "Conflict"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/409")
            detail = exception.message
        }
    }

    override fun getThrowableType(): Class<CpfAlreadyExists> {
        return CpfAlreadyExists::class.java
    }
}

@Component
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

    override fun getThrowableType(): Class<DataIntegrityViolationException> {
        return DataIntegrityViolationException::class.java
    }
}

@Component
class HandlerMethodValidationProblemDetailFactory() : ProblemDetailFactory<HandlerMethodValidationException> {

    override fun create(exception: HandlerMethodValidationException): ProblemDetail {
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

    override fun getThrowableType(): Class<HandlerMethodValidationException> {
        return HandlerMethodValidationException::class.java
    }
}

@Component
class ResourceNotFoundProblemDetailFactory() : ProblemDetailFactory<ResourceNotFoundException> {

    override fun create(exception: ResourceNotFoundException): ProblemDetail {
        return ProblemDetail.forStatus(HttpStatus.NOT_FOUND).apply {
            title = "Not Found"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/404")
            detail = exception.message
        }
    }

    override fun getThrowableType(): Class<ResourceNotFoundException> {
        return ResourceNotFoundException::class.java
    }
}

@Component
class IllegalStateProblemDetailFactory() : ProblemDetailFactory<IllegalStateException> {

    override fun create(exception: IllegalStateException): ProblemDetail {
        return ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY).apply {
            title = "Unprocessable Entity"
            type = URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/422")
            detail = exception.message
        }
    }

    override fun getThrowableType(): Class<IllegalStateException> {
        return IllegalStateException::class.java
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

    override fun getThrowableType(): Class<NoResourceFoundException> {
        return NoResourceFoundException::class.java
    }
}

@Component
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

    override fun getThrowableType(): Class<ConstraintViolationException> {
        return ConstraintViolationException::class.java
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

    override fun getThrowableType(): Class<Throwable> {
        return Throwable::class.java
    }
}

@Configuration
class ProblemDetailFactoryConfig {

    @Bean
    fun problemDetailFactoryMap(factories: List<ProblemDetailFactory<out Throwable>>): Map<Class<out Throwable>, ProblemDetailFactory<out Throwable>> {
        return factories.associateBy { it.getThrowableType() }
    }
}

@Component
class ProblemDetailSelectorFactory(
    @Autowired
    private val mapByException: Map<Class<out Throwable>, ProblemDetailFactory<out Throwable>>
) {
    fun createProblemDetailBasedOn(exception: Throwable): ProblemDetail {
        val factory = mapByException[exception::class.java] as? ProblemDetailFactory<Throwable>
        return factory?.create(exception) ?: ThrowableProblemDetailFactory().create(exception)
    }
}


