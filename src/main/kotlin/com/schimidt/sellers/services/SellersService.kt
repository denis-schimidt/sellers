package com.schimidt.sellers.services

import com.schimidt.sellers.domain.entities.Seller
import com.schimidt.sellers.domain.entities.SellerUpdatable
import com.schimidt.sellers.domain.exceptions.ResourceNotFoundException
import com.schimidt.sellers.domain.repositories.SellerRepository
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class SellersService(
    private val repository: SellerRepository
) {
    fun saveIfNotExists(seller: Seller): Result<Seller> {
        return try {
            Result.success(saveSeller(seller))

        } catch (e: DataIntegrityViolationException) {
            Result.failure(e.cause ?: e)
        }
    }

    @Transactional
    private fun saveSeller(seller: Seller): Seller {
        return repository.save<Seller>(seller)
    }

    @Transactional
    fun updateIfExists(id: Long, sellerUpdatable: SellerUpdatable): Result<Seller> {
        return try {
            repository.findById(id)
                .map { sellerRetrieved ->
                    sellerRetrieved.updateSellerUsing(sellerUpdatable)
                    repository.save<Seller>(sellerRetrieved)
                    Result.success(sellerRetrieved)
                }
                .orElseGet { Result.failure<Seller>(ResourceNotFoundException(id)) }

        } catch (e: DataIntegrityViolationException) {
            Result.failure(e.cause ?: e)
        }
    }

    fun findById(id: Long): Result<Seller> {
        return repository.findByIdWithPhones(id)
            ?.let { Result.success(it) }
            ?: Result.failure(ResourceNotFoundException(id))
    }

    fun deleteBy(id: Long) {
        repository.deleteById(id)
    }

    fun findAll(pageable: Pageable): Page<Seller> {
        return repository.findAll(pageable)
    }
}
