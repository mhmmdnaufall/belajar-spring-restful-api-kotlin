package mhmmdnaufall.restful.service

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.AddressResponse
import mhmmdnaufall.restful.model.CreateAddressRequest

interface AddressService {

    fun create(user: User, request: CreateAddressRequest): AddressResponse

}