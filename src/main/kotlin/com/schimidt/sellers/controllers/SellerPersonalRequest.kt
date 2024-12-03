package com.schimidt.sellers.controllers

import com.fasterxml.jackson.annotation.JsonProperty
import com.schimidt.sellers.domain.entities.Phone
import com.schimidt.sellers.domain.entities.PhoneId
import com.schimidt.sellers.domain.entities.PhoneType
import com.schimidt.sellers.domain.entities.Seller
import com.schimidt.sellers.domain.entities.SellerStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate

data class NewSellerRequest(
    @field:JsonProperty
    @field:Valid
    val personalData: SellerPersonalRequest,

    @field:JsonProperty
    @field:Valid
    val professionalData: NewProfessionalRequest,
) {
    fun toEntity(): Seller {
        return toEntity(personalData = personalData, cnpj = professionalData.cnpj)
    }
}

data class UpdateSellerRequest(
    @field:JsonProperty
    @field:Valid
    val personalData: SellerPersonalRequest,

    @field:JsonProperty
    @field:Valid
    val professionalData: UpdateProfessionalRequest,

    ) {
    fun toEntity(id: Long): Seller {
        return toEntity(id = id, personalData = personalData, cnpj = professionalData.cnpj, status = professionalData.status)
    }
}

private fun toEntity(id: Long? = null, personalData: SellerPersonalRequest, cnpj: String? = null, status: SellerStatus = SellerStatus.IN_ANALYSIS): Seller {
    val seller = Seller(
        id = id,
        name = personalData.name,
        email = personalData.email,
        cpf = personalData.cpf,
        cnpj = cnpj,
        birthday = personalData.birthday,
        phones = mutableListOf(),
        status = status
    )
    personalData.phones.forEach { phoneRequest ->
        seller.addPhone(phoneRequest.toEntity(seller))
    }
    return seller
}

data class UpdateProfessionalRequest(
    @field:CNPJ
    @field:Size(max = 20)
    val cnpj: String? = null,

    @field:NotNull
    val status: SellerStatus
)

data class NewProfessionalRequest(
    @field:CNPJ
    @field:Size(max = 20)
    val cnpj: String? = null,
)

data class SellerPersonalRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val name: String,

    @field:NotBlank
    @field:Size(max = 100)
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Size(max = 15)
    @field:CPF
    val cpf: String,

    @field:NotNull
    @field:Past
    val birthday: LocalDate,

    @field:NotEmpty
    @field:Size(min = 1, max = 2)
    @field:Valid
    val phones: List<PhoneRequest>
)

data class PhoneRequest(
    @field:NotNull
    @field:Min(10)
    @field:Max(99)
    @field:JsonProperty("area_code")
    val areaCode: Int,

    @field:NotNull
    @field:Min(1999_9999)
    @field:Max(9999_99999)
    val number: Int,

    @field:NotNull
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
