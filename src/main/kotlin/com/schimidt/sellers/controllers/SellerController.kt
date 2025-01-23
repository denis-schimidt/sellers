package com.schimidt.sellers.controllers

import com.schimidt.sellers.controllers.handlers.ExceptionHandler.ProblemDetailCustom
import com.schimidt.sellers.controllers.handlers.ExceptionHandler.ProblemDetailWithViolationsCustom
import com.schimidt.sellers.controllers.handlers.ProblemDetailSelectorFactory
import com.schimidt.sellers.controllers.helpers.OrderingDirectionRequest
import com.schimidt.sellers.controllers.helpers.OrderingFieldRequest
import com.schimidt.sellers.controllers.helpers.PageResponse
import com.schimidt.sellers.domain.entities.Seller
import com.schimidt.sellers.services.SellersService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn.PATH
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.PageRequest
import org.springframework.http.CacheControl
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.MatrixVariable
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/v1", produces = [APPLICATION_JSON_VALUE])
class SellerController(
    private val service: SellersService,
    private val problemDetailSelectorFactory: ProblemDetailSelectorFactory
) : EndpointDocumented {

    @PostMapping("/sellers", consumes = [APPLICATION_JSON_VALUE])
    override fun saveSeller(@Validated(value = [ValidateAll::class]) @RequestBody sellerRequest: NewSellerRequest): Any {
        return service.saveIfNotExists(sellerRequest.toEntity())
            .onSuccess {
                return ResponseEntity.created(
                    ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(it.id)
                        .toUri()
                ).body(SellerResponse.from(it))
            }
            .onFailure { return problemDetailSelectorFactory.createProblemDetailBasedOn(it) }
    }

    @PutMapping("/sellers/{id}", consumes = [APPLICATION_JSON_VALUE])
    override fun updateSellerTotally(
        @Positive @PathVariable id: Long,
        @Validated(value = [ValidateAll::class]) @RequestBody sellerUpdatable: UpdateSellerRequest
    ): Any {
        return executeUpdateSeller(id, sellerUpdatable)
    }

    @PatchMapping("/sellers/{id}", consumes = [APPLICATION_JSON_VALUE])
    override fun updateSellerPartially(@Positive @PathVariable id: Long, @Validated @RequestBody sellerUpdatable: UpdateSellerRequest): Any {
        return executeUpdateSeller(id, sellerUpdatable)
    }

    private fun executeUpdateSeller(id: Long, sellerUpdatable: UpdateSellerRequest): Any {
        return service.updateIfExists(id, sellerUpdatable)
            .onSuccess { return ResponseEntity.ok(SellerResponse.from(it)) }
            .onFailure { return problemDetailSelectorFactory.createProblemDetailBasedOn(it) }
    }

    @GetMapping("/sellers/{id}")
    override fun getSellerBy(@Positive @PathVariable id: Long): Any {
        return service.findById(id)
            .onSuccess {
                return ResponseEntity.ok()
                    .cacheControl(
                        CacheControl.maxAge(5, TimeUnit.MINUTES)
                            .noTransform()
                            .cachePrivate()
                    )
                    .body(SellerResponse.from(it))
            }
            .onFailure { return problemDetailSelectorFactory.createProblemDetailBasedOn(it) }
    }

    data class InputDocument(val cpf: String?, val cnpj: String?)

    @GetMapping("/sellers;{documents}")
    override fun getSellersBy(
        @MatrixVariable documents: Map<String, List<String>>
    ): Any {

        println(documents)
//        if (StringUtils.isNotBlank(cpf)) {
//            return service.findByCpf(cpf!!)
//                .onSuccess { return ResponseEntity.ok(SellerResponse.from(it)) }
//                .onFailure { return problemDetailSelectorFactory.createProblemDetailBasedOn(it) }
//        }
//
//        if (StringUtils.isNotBlank(cnpj)) {
//            return service.findByCnpj(cnpj!!)
//                .onSuccess { return ResponseEntity.ok(SellerResponse.from(it)) }
//                .onFailure { return problemDetailSelectorFactory.createProblemDetailBasedOn(it) }
//        }

        return Result.failure<Seller?>(IllegalArgumentException("CPF or CNPJ must be provided"))
    }

    @GetMapping("/sellers")
    override fun getAllSellers(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "5") @Max(10) size: Int,
        @RequestParam(value = "orderBy", defaultValue = "ID") orderBy: OrderingFieldRequest,
        @RequestParam(value = "direction", defaultValue = "DESC") direction: OrderingDirectionRequest
    ): Any {
        val pageRequest = PageRequest.of(page, size, direction.searchParameter, orderBy.name.lowercase())
        return service.findAll(pageRequest)
            .map(SellerResponse::from)
            .let { ResponseEntity.ok(PageResponse.from(it)) }
    }

    @DeleteMapping("/sellers/{id}")
    override fun deleteSeller(@Positive @PathVariable id: Long): Any {
        return service.deleteBy(id)
            .let { ResponseEntity.noContent().build<SellerResponse>() }
    }
}

@Tag(name = "Sellers", description = "Operations related to sellers")
private interface EndpointDocumented {

    @Operation(
        summary = "Save a new seller", tags = ["Sellers"]
    )
    @ApiResponses(
        ApiResponse(
            headers = [Header(
                name = "Location",
                description = "The URI of the created seller"
            )],
            responseCode = "201",
            description = "Seller created",
            content = [Content(schema = Schema(implementation = SellerResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid request",
            content = [Content(schema = Schema(implementation = ProblemDetailWithViolationsCustom::class))]
        ),
        ApiResponse(
            responseCode = "409",
            description = "Conflict with another seller",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        )
    )
    fun saveSeller(sellerRequest: NewSellerRequest): Any

    @Operation(
        summary = "Update a seller totally", tags = ["Sellers"]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Seller updated",
            content = [Content(schema = Schema(implementation = SellerResponse::class))]
        ),
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
    fun updateSellerTotally(@Positive @PathVariable id: Long, @Validated @RequestBody sellerUpdatable: UpdateSellerRequest): Any

    @Operation(
        summary = "Update a seller partially", tags = ["Sellers"]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Seller updated",
            content = [Content(schema = Schema(implementation = SellerResponse::class))]
        ),
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
    fun updateSellerPartially(@Positive @PathVariable id: Long, @Validated @RequestBody sellerUpdatable: UpdateSellerRequest): Any

    @Operation(
        summary = "Retrieve a seller by id", tags = ["Sellers"]
    )
    @ApiResponses(
        ApiResponse(
            headers = [Header(
                name = "Cache-Control (max-age)",
                description = "The maximum time in minutes that the response can be cached"
            )],
            responseCode = "200",
            description = "Seller found",
            content = [Content(schema = Schema(implementation = SellerResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad Request - Invalid input parameter",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Seller not found",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        )
    )
    fun getSellerBy(@Positive @PathVariable("id") id: Long): Any

    @Operation(
        summary = "Retrieves one or more sellers using documents (matrix variable)",
        description = "You can provide both CPFs and CNPJs at same time from different sellers (cpf=77135947010,55608789016;cnpj=49781729000131).  " +
                "If you prefer, you can chose only one type for query (cpf or cnpj).",
        tags = ["Sellers"],
        hidden = false,
        parameters = [
            Parameter(
                name = "documents",
                required = true,
                hidden = false,
                example = "cpf=77135947010,55608789016;cnpj=49781729000131,12191314000106,03506261000176",
                `in` = PATH,
            )
        ]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Seller found",
            content = [Content(schema = Schema(implementation = SellerResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad Request - Invalid input parameter",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Seller not found",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        )
    )
    fun getSellersBy(
        @MatrixVariable documents: Map<String, List<String>>
    ): Any

    @Operation(
        summary = "List all sellers paginated", tags = ["Sellers"],
        parameters = [
            Parameter(name = "page", description = "The page number", example = "0"),
            Parameter(name = "size", description = "The page size", example = "5"),
            Parameter(name = "orderBy", description = "The field to order by", example = "ID"),
            Parameter(name = "direction", description = "The direction to order", example = "DESC")
        ]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Seller found",
            content = [Content(schema = Schema(implementation = SellerResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        )
    )
    @GetMapping
    fun getAllSellers(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "5") @Max(10) size: Int,
        @RequestParam(value = "orderBy") @Schema(allowableValues = ["id", "email", "cpf", "cnpj"]) orderBy: OrderingFieldRequest,
        @RequestParam(value = "direction") @Schema(allowableValues = ["asc", "desc"]) direction: OrderingDirectionRequest
    ): Any

    @Operation(
        summary = "Delete a seller by id", tags = ["Sellers"],
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "204",
            description = "Seller deleted successfully",
            content = [Content(schema = Schema(implementation = SellerResponse::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(schema = Schema(implementation = ProblemDetailCustom::class))]
        )
    )
    fun deleteSeller(@Positive @PathVariable id: Long): Any
}
