package com.schimidt.sellers.controllers.helpers

import org.springframework.data.domain.Sort

enum class OrderingDirectionRequest(val searchParameter: Sort.Direction) {
    ASC(Sort.Direction.ASC),
    DESC(Sort.Direction.DESC),
}

enum class OrderingFieldRequest() {
    ID,
    EMAIL,
    CPF,
    CNPJ
}
