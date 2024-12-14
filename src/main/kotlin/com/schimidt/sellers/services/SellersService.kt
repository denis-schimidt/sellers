package com.schimidt.sellers.services

import com.schimidt.sellers.domain.entities.Seller
import com.schimidt.sellers.domain.entities.SellerUpdatable
import com.schimidt.sellers.domain.exceptions.CpfAlreadyExists
import com.schimidt.sellers.domain.exceptions.ResourceNotFoundException
import com.schimidt.sellers.domain.repositories.SellerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SellersService(
    private val repository: SellerRepository
) {
    fun saveIfNotExists(seller: Seller): Result<Seller> {
        repository.findByCpf(seller.cpf())?.let {
            return Result.failure(CpfAlreadyExists(seller.cpf()))
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
    fun updateIfExists(id: Long, sellerUpdatable: SellerUpdatable): Result<Seller> {
        return try {
            repository.findById(id)
                .map { sellerRetrieved ->
                    sellerRetrieved.updateSellerUsing(sellerUpdatable)
                    repository.save<Seller>(sellerRetrieved)
                    Result.success(sellerRetrieved)
                }
                .orElseGet { Result.failure<Seller>(ResourceNotFoundException(id)) }

        } catch (e: Exception) {
            Result.failure(e.cause?.cause ?: e)
        }
    }

    fun findById(id: Long): Result<Seller> {

        return try {
            repository.findByIdWithPhones(id)?.let { Result.success(it) }
                ?: Result.failure(ResourceNotFoundException(id))

        } catch (e: Exception) {
            Result.failure(e.cause ?: e)
        }
    }

    fun deleteBy(lng: Long): Result<Unit> {

        return try {
            repository.deleteById(lng)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e.cause ?: e)
        }
    }
}
