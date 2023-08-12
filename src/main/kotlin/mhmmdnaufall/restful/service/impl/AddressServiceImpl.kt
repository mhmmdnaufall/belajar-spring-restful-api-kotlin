package mhmmdnaufall.restful.service.impl

import mhmmdnaufall.restful.entity.Address
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.AddressResponse
import mhmmdnaufall.restful.model.CreateAddressRequest
import mhmmdnaufall.restful.repository.AddressRepository
import mhmmdnaufall.restful.repository.ContactRepository
import mhmmdnaufall.restful.service.AddressService
import mhmmdnaufall.restful.service.ValidationService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class AddressServiceImpl(
        private val contactRepository: ContactRepository,
        private val addressRepository: AddressRepository,
        private val validationService: ValidationService
) : AddressService {

    @Transactional
    override fun create(user: User, request: CreateAddressRequest): AddressResponse {
        validationService.validate(request)

        val contact = contactRepository.findFirstByUserAndId(user, request.contactId!!)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found")

        val address = Address(
                id = UUID.randomUUID().toString(),
                contact = contact,
                street = request.street,
                city = request.city,
                province = request.province,
                country = request.country,
                postalCode = request.postalCode
        )

        addressRepository.save(address)

        return toAddressResponse(address)
    }

    private fun toAddressResponse(address: Address): AddressResponse {
        return AddressResponse(
                id = address.id,
                street = address.street,
                city = address.city,
                province = address.province,
                country = address.country,
                postalCode = address.postalCode
        )
    }

}