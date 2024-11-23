package com.schimidt.sellers.services

import com.schimidt.sellers.entities.Seller
import com.schimidt.sellers.entities.SellerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SellersService(private val repository: SellerRepository) {

    @Transactional
    fun save(seller: Seller): Seller {
        return repository.save<Seller>(seller)
    }

    @Transactional
    fun update(seller: Seller): Result<Seller> {

        if (repository.findById(seller.id!!).isEmpty.not()) {
            return Result.failure(IllegalArgumentException("Seller ID:${seller.id} not found"))
        }

        return Result.success(repository.save<Seller>(seller))
    }
}