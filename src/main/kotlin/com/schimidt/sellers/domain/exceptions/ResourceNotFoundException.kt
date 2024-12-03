package com.schimidt.sellers.domain.exceptions

class ResourceNotFoundException(id: Long) : RuntimeException() {
    override val message: String = "Seller ID -> $id not found"
}
