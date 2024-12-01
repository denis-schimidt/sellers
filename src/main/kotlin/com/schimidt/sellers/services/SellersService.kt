package com.schimidt.sellers.services

import com.schimidt.sellers.domain.entities.Seller
import com.schimidt.sellers.domain.exceptions.CpfAlreadyExists
import com.schimidt.sellers.domain.repositories.SellerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SellersService(
    private val repository: SellerRepository
) {
    fun saveIfNotExists(seller: Seller): Result<Seller> {
        repository.findByCpf(seller.cpf)?.let {
            return Result.failure(CpfAlreadyExists(seller.cpf))
        }

        return try {
            Result.success(saveSeller(seller))
        } catch (e: Exception) {
            Result.failure(e.cause ?: e)
        }
    }

    @Transactional
    private fun saveSeller(seller: Seller): Seller {
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
