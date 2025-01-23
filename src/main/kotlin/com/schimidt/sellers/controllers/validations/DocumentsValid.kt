package com.schimidt.sellers.controllers.validations

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DocumentMapValidator::class])
annotation class DocumentsValid(
    val message: String = "Invalid document(s) in map. Check if the keys are 'cpf' or 'cnpj' and the values are valid.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
