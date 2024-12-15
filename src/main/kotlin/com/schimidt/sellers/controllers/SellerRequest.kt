package com.schimidt.sellers.controllers

import com.fasterxml.jackson.annotation.JsonProperty
import com.schimidt.sellers.domain.entities.Phone
import com.schimidt.sellers.domain.entities.PhoneId
import com.schimidt.sellers.domain.entities.PhoneType
import com.schimidt.sellers.domain.entities.Seller
import com.schimidt.sellers.domain.entities.SellerStatus
import com.schimidt.sellers.domain.entities.SellerUpdatable
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate

interface ValidateAll

data class NewSellerRequest(
    @field:JsonProperty
    @field:Valid
    val personalData: SellerPersonalRequest,

    @field:JsonProperty
    @field:Valid
    val professionalData: NewProfessionalRequest,
) {
    fun toEntity(): Seller {
        val seller = Seller(
            name = personalData.name!!,
            email = personalData.email!!,
            cpf = personalData.cpf!!,
            cnpj = professionalData.cnpj,
            birthday = personalData.birthday!!,
            phones = mutableSetOf(),
            status = SellerStatus.IN_ANALYSIS
        )

        personalData.phones?.forEach { phoneRequest ->
            seller.addPhone(phoneRequest.toEntity(seller))
        }

        return seller
    }
}

data class UpdateSellerRequest(
    @field:JsonProperty
    @field:Valid
    val personalData: SellerPersonalRequest?,

    @field:JsonProperty
    @field:Valid
    val professionalData: UpdateProfessionalRequest?
) : SellerUpdatable {

    override fun name(): String? = personalData?.name

    override fun email(): String? = personalData?.email

    override fun cpf(): String? = personalData?.cpf

    override fun birthday(): LocalDate? = personalData?.birthday

    override fun status(): SellerStatus? = professionalData?.status

    override fun cnpj(): String? = professionalData?.cnpj

    override fun phones(): Set<Phone>? {
        return personalData?.phones
            ?.map { Phone(type = it.type, id = PhoneId(areaCode = it.areaCode.toByte(), number = it.number)) }
            ?.toSet() ?: emptySet()
    }

    override fun hasDifferentStatus(otherStatus: SellerStatus): Boolean {
        return status() != null && status() != otherStatus
    }
}

data class UpdateProfessionalRequest(
    @field:CNPJ
    @field:Size(max = 20)
    val cnpj: String? = null,

    @field:NotNull(groups = [ValidateAll::class])
    val status: SellerStatus = SellerStatus.IN_ANALYSIS
)

data class NewProfessionalRequest(
    @field:CNPJ
    @field:Size(max = 20)
    val cnpj: String? = null,
)

data class SellerPersonalRequest(
    @field:NotBlank(groups = [ValidateAll::class])
    @field:Size(max = 100)
    val name: String?,

    @field:NotBlank(groups = [ValidateAll::class])
    @field:Size(max = 100)
    @field:Email
    val email: String?,

    @field:NotBlank(groups = [ValidateAll::class])
    @field:Size(max = 15)
    @field:CPF
    val cpf: String?,

    @field:NotNull(groups = [ValidateAll::class])
    @field:Past
    val birthday: LocalDate?,

    @field:Size(min = 1, max = 2, groups = [ValidateAll::class])
    @field:Valid
    val phones: List<PhoneRequest>?
)

data class PhoneRequest(
    @field:Min(10)
    @field:Max(99)
    @field:JsonProperty("area_code")
    val areaCode: Int,

    @field:Min(1999_9999)
    @field:Max(9999_99999)
    val number: Int,

    @field:JsonProperty("phone_type")
    val type: PhoneType
) {
    fun toEntity(seller: Seller): Phone {
        return Phone(
            id = PhoneId(
                areaCode = areaCode.toByte(),
                number = number
            ),
            seller = seller,
            type = type
        )
    }
}
