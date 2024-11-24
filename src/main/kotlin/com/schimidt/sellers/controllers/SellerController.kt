package com.schimidt.sellers.controllers

import com.schimidt.sellers.controllers.ExceptionHandler.ProblemDetailCustom
import com.schimidt.sellers.controllers.ExceptionHandler.ProblemDetailWithViolationsCustom
import com.schimidt.sellers.services.SellersService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/sellers", consumes = ["application/json"], produces = ["application/json"])
class SellerController(private val service: SellersService) : EndpointDocumented {

    @PostMapping
    override fun saveSeller(@Validated @RequestBody sellerRequest: NewSellerRequest): ResponseEntity<SellerResponse> {
        return service.save(sellerRequest.toEntity())
            .let {
                ResponseEntity.created(
                    ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(it.id)
                        .toUri()
                ).body(SellerResponse.from(it))
            }
    }

    @PutMapping("/{id}")
    override fun updateSeller(@Positive id: Long, @Validated @RequestBody sellerRequest: UpdateSellerRequest): ResponseEntity<Any> {
        val result = service.update(sellerRequest.toEntity(id))

        if (1 == 5) {
            return ResponseEntity.ok().build()
        }

        return result.fold(
            { ResponseEntity.ok(SellerResponse.from(it)) },
            { ResponseEntity.status(UNPROCESSABLE_ENTITY).body(it.message) }
        )
    }
}


@Tag(name = "Sellers", description = "Operations related to sellers")
private interface EndpointDocumented {

    @Operation(
        summary = "Save a new seller", tags = ["Sellers"]
    )
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Seller created"),
        ApiResponse(
            responseCode = "400",
            description = "Invalid request",
            content = [Content(schema = Schema(implementation = ProblemDetailWithViolationsCustom::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Deu Ruim",
            content = [Content(schema = Schema(implementation = ProblemDetailWithViolationsCustom::class))]
        )
    )
    fun saveSeller(sellerRequest: NewSellerRequest): ResponseEntity<SellerResponse>

    @Operation(
        summary = "Update a seller", tags = ["Sellers"]
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Seller updated"),
        ApiResponse(
            responseCode = "400",
            description = "Invalid request",
            content = [Content(schema = Schema(implementation = ProblemDetailWithViolationsCustom::class))]
        ),
        ApiResponse(
            responseCode = "422", description = "Seller not found", content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Unknown error",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        )
    )
    fun updateSeller(@Positive id: Long, @Valid @RequestBody sellerRequest: UpdateSellerRequest): ResponseEntity<Any>
}
