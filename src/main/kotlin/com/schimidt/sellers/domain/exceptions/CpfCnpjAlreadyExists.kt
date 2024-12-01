package com.schimidt.sellers.domain.exceptions

class CpfCnpjAlreadyExists(cpfCnpj: String) : RuntimeException() {
    override val message: String = "Seller CPF/CNPJ: $cpfCnpj already exists"
}
