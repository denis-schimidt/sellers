package com.schimidt.sellers.controllers.validations

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

class DocumentMapValidator : ConstraintValidator<DocumentsValid, Any> {

    override fun isValid(
        inputParameters: Any,
        context: ConstraintValidatorContext?
    ): Boolean {
        inputParameters as Map<String, List<String>>

        if (inputParameters.isEmpty()) return true

        val hasInvalidKey = inputParameters.keys
            .any { it.lowercase().matches(Regex("cpf|cnpj")).not() }

        if (hasInvalidKey) return false

        return isCpfsValidWhenItExists(inputParameters["cpf"]) && isCnpjsValidWhenItExists(inputParameters["cnpj"])
    }

    private fun isCpfsValidWhenItExists(cpfs: List<String>?): Boolean {
        cpfs?.let {
            val cpfValidator = CPFValidator().apply { initialize(null) }
            return it.all { cpf -> cpfValidator.isValid(cpf, null) }
        }
        return true
    }

    private fun isCnpjsValidWhenItExists(cnpjs: List<String>?): Boolean {
        cnpjs?.let {
            val cnpjValidator = CNPJValidator().apply { initialize(null) }
            return it.all { cnpj -> cnpjValidator.isValid(cnpj, null) }
        }
        return true
    }
}
