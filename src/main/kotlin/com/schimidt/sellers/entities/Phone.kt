package com.schimidt.sellers.entities

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Entity
@Table(name = "phones")
data class Phone(
    @EmbeddedId
    @field:Valid
    val id: PhoneId,

    @ManyToOne
    @JoinColumn(name = "seller_id", foreignKey = ForeignKey(name = "fk_phones_sellers"), nullable = false)
    var seller: Seller? = null,

    @Enumerated(STRING)
    @Column(nullable = false, length = 15)
    val type: PhoneType
) {
    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is Phone -> false
            else -> id == other.id
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun isCellPhone() = type == PhoneType.CELL_PHONE

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(EmbeddedId = $id )"
    }
}

enum class PhoneType {
    HOME,
    CELL_PHONE,
    WORK
}

@Embeddable
data class PhoneId(

    @field:Min(10)
    @field:Max(99)
    @Column(name = "area_code", nullable = false)
    val areaCode: Byte,

    @field:Min(100_0000)
    @field:Max(9999_99999)
    @Column(name = "number", nullable = false)
    val number: Int
) {

    @Override
    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is PhoneId -> false
            else -> areaCode == other.areaCode && number == other.number
        }
    }

    @Override
    override fun hashCode(): Int {
        var result = areaCode.toInt()
        result = 31 * result + number
        return result
    }

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(areaCode = $areaCode, number = $number)"
    }
}
