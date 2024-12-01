package com.schimidt.sellers.domain.exceptions

class CpfAlreadyExists(cpfCnpj: String) : RuntimeException() {
    override val message: String = "Seller CPF -> $cpfCnpj already exists"
}
