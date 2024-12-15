package com.schimidt.sellers.controllers.helpers

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Sort

enum class OrderingDirectionRequest(val searchParameter: Sort.Direction) {
    @field:Schema(name = "asc")
    ASC(Sort.Direction.ASC),

    @field:Schema(name = "desc")
    DESC(Sort.Direction.DESC),
}

enum class OrderingFieldRequest(val fieldName: String) {
    @field:Schema(name = "id")
    ID("id"),

    @field:Schema(name = "email")
    EMAIL("email"),

    @field:Schema(name = "cpf")
    CPF("cpf"),
}
