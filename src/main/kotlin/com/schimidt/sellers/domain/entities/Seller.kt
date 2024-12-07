package com.schimidt.sellers.domain.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate

@Table(name = "sellers")
@Entity
class Seller(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,

    @field:NotBlank
    @Column(nullable = false, length = 100)
    val name: String,

    @Column(nullable = false, length = 100, unique = true)
    @field:Email
    @field:NotBlank
    val email: String,

    @field:CPF
    @field:NotBlank
    @Column(nullable = false, length = 15, unique = true)
    val cpf: String,

    @field:Past
    @Column(nullable = false)
    val birthday: LocalDate,

    @Enumerated(STRING)
    @Column(nullable = false, length = 20)
    private var status: SellerStatus = SellerStatus.IN_ANALYSIS,

    @field:CNPJ
    @Column(nullable = true, length = 20, unique = true)
    val cnpj: String? = null,

    @OneToMany(mappedBy = "seller", cascade = [CascadeType.ALL], orphanRemoval = true)
    @field:Size(min = 1, max = 2)
    @field:Valid
    val phones: MutableList<Phone> = mutableListOf()
) {
    fun status(): SellerStatus = status

    fun createNewSellerUpdatedUsing(seller: Seller): Seller {
        return Seller(
            id = this.id,
            name = seller.name,
            email = seller.email,
            cpf = this.cpf,
            birthday = seller.birthday,
            cnpj = seller.cnpj,
            phones = seller.phones
        ).apply {
            if (this.status != seller.status) {
                changeStatus(seller.status)
            }
        }
    }

    fun approve() {
        changeStatus(SellerStatus.ACTIVE)
    }

    fun fraudDetected() {
        changeStatus(SellerStatus.BLOCKED)
    }

    fun inactivate(lastSell: LocalDate) {
        val oneYearAgo = LocalDate.now().minusYears(1)

        if (lastSell.isAfter(oneYearAgo)) {
            throw IllegalStateException("Seller cannot be inactive")
        }
        changeStatus(SellerStatus.INACTIVE)
    }

    fun changeStatus(newStatus: SellerStatus) {
        if (!status.canBeChanged(newStatus)) {
            throw IllegalStateException("Status $status cannot be changed to $newStatus")
        }

        status = newStatus
    }

    fun isActive(): Boolean = status == SellerStatus.ACTIVE

    fun addPhone(phone: Phone) {
        phone.seller = this
        phones.add(phone)
    }

    fun removePhone(phone: Phone) {
        phone.seller = null
        phones.remove(phone)
    }
}

enum class SellerStatus {
    IN_ANALYSIS,
    BLOCKED,
    ACTIVE,
    INACTIVE;

    fun canBeChanged(newStatus: SellerStatus): Boolean {
        return when (this) {
            IN_ANALYSIS -> newStatus == ACTIVE || newStatus == BLOCKED
            ACTIVE -> newStatus == BLOCKED || newStatus == INACTIVE
            BLOCKED -> newStatus == ACTIVE || newStatus == INACTIVE
            INACTIVE -> newStatus == ACTIVE
        }
    }
}
