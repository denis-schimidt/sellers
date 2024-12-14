package com.schimidt.sellers.domain.entities

import java.time.LocalDate

interface SellerUpdatable {

    fun name(): String?

    fun email(): String?

    fun cpf(): String?

    fun birthday(): LocalDate?

    fun status(): SellerStatus?

    fun cnpj(): String?

    fun phones(): Set<Phone>?

    fun hasDifferentStatus(otherStatus: SellerStatus): Boolean

    fun hasPhones(): Boolean {
        return phones()?.isNotEmpty() == true
    }
}
