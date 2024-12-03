package com.schimidt.sellers.domain.repositories

import com.schimidt.sellers.domain.entities.Seller
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SellerRepository : JpaRepository<Seller, Long> {
    fun findByCpf(cpf: String): Seller?

    @Query("SELECT s FROM Seller s LEFT JOIN FETCH s.phones WHERE s.id = :id")
    fun findByIdWithPhones(@Param("id") id: Long): Seller?
}
