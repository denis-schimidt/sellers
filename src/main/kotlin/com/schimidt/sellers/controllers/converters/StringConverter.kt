package com.schimidt.sellers.controllers.converters

import com.schimidt.sellers.controllers.helpers.OrderingDirectionRequest
import com.schimidt.sellers.controllers.helpers.OrderingFieldRequest
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToDirectionConverter : Converter<String, OrderingDirectionRequest> {

    override fun convert(source: String): OrderingDirectionRequest {
        return OrderingDirectionRequest.valueOf(source.uppercase())
    }
}

@Component
class StringToField : Converter<String, OrderingFieldRequest> {

    override fun convert(source: String): OrderingFieldRequest {
        return OrderingFieldRequest.valueOf(source.uppercase())
    }
}
