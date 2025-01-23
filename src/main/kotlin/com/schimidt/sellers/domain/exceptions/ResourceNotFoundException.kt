package com.schimidt.sellers.domain.exceptions

class ResourceNotFoundException : RuntimeException {
    constructor(id: Long) : super("Seller ID -> $id not found")
    constructor(cpf: String) : super("Seller CPF -> $cpf not found")
}
