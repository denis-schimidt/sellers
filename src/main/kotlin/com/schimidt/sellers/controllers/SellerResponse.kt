package com.schimidt.sellers.controllers

import com.fasterxml.jackson.annotation.JsonProperty
import com.schimidt.sellers.entities.Phone
import com.schimidt.sellers.entities.Seller
import java.time.LocalDate

data class SellerResponse(
    val id: Long,
    val name: String,
    val email: String,
    val cpf: String,
    val birthday: LocalDate,
    val phones: List<PhoneResponse>
) {

    companion object {
        fun from(seller: Seller): SellerResponse {
            return SellerResponse(
                id = seller.id!!,
                name = seller.name,
                email = seller.email,
                cpf = seller.cpf,
                birthday = seller.birthday,
                phones = seller.phones.map { PhoneResponse.from(it) }
            )
        }
    }
}

data class PhoneResponse(
    @JsonProperty("area_code")
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
