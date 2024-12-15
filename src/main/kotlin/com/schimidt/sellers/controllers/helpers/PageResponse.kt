package com.schimidt.sellers.controllers.helpers

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.Page

data class PageDetailsResponse<T>(
    val totalPages: Int = 0,
    val totalElements: Long = 0,
    val number: Int = 0,
    val size: Int = 0,
    val orderBy: String? = null,
    val direction: String? = null
) {
    companion object {
        fun <T> from(page: Page<T>): PageDetailsResponse<T> {
            return PageDetailsResponse(
                totalPages = page.totalPages,
                totalElements = page.totalElements,
                number = page.number,
                size = page.size,
                orderBy = page.sort.toString().split(":")[0],
                direction = page.sort.toString().split(":")[1].lowercase()
            )
        }
    }
}

data class PageResponse<T>(
    @JsonProperty("page")
    val page: PageDetailsResponse<T>,
    val content: List<T> = emptyList()
) {
    companion object {
        fun <T> from(page: Page<T>): PageResponse<T> {
            return PageResponse(page = PageDetailsResponse.from(page), content = page.content)
        }
    }
}
