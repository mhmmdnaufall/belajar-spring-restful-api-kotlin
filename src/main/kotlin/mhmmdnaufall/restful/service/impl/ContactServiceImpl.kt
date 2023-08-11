package mhmmdnaufall.restful.service.impl

import mhmmdnaufall.restful.entity.Contact
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.ContactResponse
import mhmmdnaufall.restful.model.CreateContactRequest
import mhmmdnaufall.restful.repository.ContactRepository
import mhmmdnaufall.restful.service.ContactService
import mhmmdnaufall.restful.service.ValidationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ContactServiceImpl(
        private val contactRepository: ContactRepository,
        private val validationService: ValidationService
) : ContactService {

    @Transactional
    override fun create(user: User, request: CreateContactRequest): ContactResponse {
        validationService.validate(request)

        val contact = Contact(
                id = UUID.randomUUID().toString(),
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                phone = request.phone,
                user = user
        )

        contactRepository.save(contact)

        return ContactResponse(
                id = contact.id,
                firstName = contact.firstName,
                lastName = contact.lastName,
                email = contact.email,
                phone = contact.phone
        )
    }

}