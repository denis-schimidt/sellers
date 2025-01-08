package com.schimidt.sellers.controllers

import com.fasterxml.jackson.annotation.JsonProperty
import com.schimidt.sellers.domain.entities.Phone
import com.schimidt.sellers.domain.entities.Seller
import java.time.LocalDate

data class SellerResponse(
    @JsonProperty("personal_data")
    val personalResponse: SellerPersonalResponse,
    @JsonProperty("professional_data")
    val professionalResponse: SellerProfissionalResponse
) {
    companion object {
        fun from(seller: Seller): SellerResponse {
            return SellerResponse(
                personalResponse = SellerPersonalResponse.from(seller),
                professionalResponse = SellerProfissionalResponse.from(seller)
            )
        }
    }
}

data class SellerProfissionalResponse(
    val cnpj: String?,
    val status: String
) {
    companion object {
        fun from(seller: Seller): SellerProfissionalResponse {
            return SellerProfissionalResponse(
                cnpj = seller.cnpj(),
                status = seller.status().name
            )
        }
    }
}

data class SellerPersonalResponse(
    val id: Long,
    val name: String,
    val email: String,
    val cpf: String,
    val birthday: LocalDate,
    val phones: List<PhoneResponse>
) {
    companion object {
        fun from(seller: Seller): SellerPersonalResponse {
            return SellerPersonalResponse(
                id = seller.id!!,
                name = seller.name(),
                email = seller.email(),
                cpf = seller.cpf(),
                birthday = seller.birthday(),
                phones = seller.phones().map { PhoneResponse.from(it) }
            )
        }
    }
}

data class PhoneResponse(
    @field:JsonProperty("area_code")
    val areaCode: Byte,
    val number: Int,
    @field:JsonProperty("phone_type")
    val type: String
) {
    companion object {
        fun from(phone: Phone): PhoneResponse {
            return PhoneResponse(
                areaCode = phone.id.areaCode,
                number = phone.id.number,
                type = phone.type.name
            )
        }
    }
}

