package com.schimidt.sellers.domain.repositories

import com.schimidt.sellers.domain.entities.Seller
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SellerRepository : JpaRepository<Seller, Long> {
    fun findByCpf(cpf: String): Seller?
}
