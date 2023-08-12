package mhmmdnaufall.restful.controller

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.AddressResponse
import mhmmdnaufall.restful.model.CreateAddressRequest
import mhmmdnaufall.restful.model.UpdateAddressRequest
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.service.AddressService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AddressController( private val addressService: AddressService ) {

    @PostMapping(
            path = ["/api/contacts/{contactId}/addresses"],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(
            user: User,
            @RequestBody request: CreateAddressRequest,
            @PathVariable("contactId") contactId: String
    ): WebResponse<AddressResponse> {

        request.contactId = contactId
        val response = addressService.create(user, request)
        return WebResponse(data = response)

    }

    @GetMapping(
            path = ["/api/contacts/{contactId}/addresses/{addressId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun get(
            user: User,
            @PathVariable("contactId") contactId: String,
            @PathVariable("addressId") addressId: String
    ): WebResponse<AddressResponse> {
        val response = addressService.get(user, contactId, addressId)
        return WebResponse(data = response)
    }

    @PutMapping(
            path = ["/api/contacts/{contactId}/addresses/{addressId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun update(
            user: User,
            @RequestBody request: UpdateAddressRequest,
            @PathVariable("contactId") contactId: String,
            @PathVariable("contactId") addressId: String
    ): WebResponse<AddressResponse> {

        request.contactId = contactId
        request.addressId = addressId

        val response = addressService.update(user, request)
        return WebResponse(data = response)

    }

}