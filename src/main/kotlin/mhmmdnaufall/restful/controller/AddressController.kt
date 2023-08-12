package mhmmdnaufall.restful.controller

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.AddressResponse
import mhmmdnaufall.restful.model.CreateAddressRequest
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.service.AddressService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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

}