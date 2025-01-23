package com.schimidt.sellers.domain.repositories

import com.schimidt.sellers.domain.entities.Seller
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SellerRepository : JpaRepository<Seller, Long> {

    @EntityGraph("Seller.phones")
    fun findSellerByCpf(cpf: String): Seller?

    @EntityGraph("Seller.phones")
    fun findSellerByCnpj(cnpj: String): Seller?

    @EntityGraph("Seller.phones")
    fun findSellerById(@Param("id") id: Long): Seller?
}
