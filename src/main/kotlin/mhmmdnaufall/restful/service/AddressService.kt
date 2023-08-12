package mhmmdnaufall.restful.service

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.AddressResponse
import mhmmdnaufall.restful.model.CreateAddressRequest
import mhmmdnaufall.restful.model.UpdateAddressRequest

interface AddressService {

    fun create(user: User, request: CreateAddressRequest): AddressResponse

    fun get(user: User, contactId: String, addressId: String): AddressResponse

    fun update(user: User, request: UpdateAddressRequest): AddressResponse

    fun remove(user: User, contactId: String, addressId: String)

}