package com.schimidt.sellers.entities

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SellerRepository : JpaRepository<Seller, Long> {
    fun findByCpf(cpf: String): Seller?
}
