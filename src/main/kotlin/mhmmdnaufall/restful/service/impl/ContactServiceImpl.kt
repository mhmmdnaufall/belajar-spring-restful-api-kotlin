package mhmmdnaufall.restful.service.impl

import mhmmdnaufall.restful.entity.Contact
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.ContactResponse
import mhmmdnaufall.restful.model.CreateContactRequest
import mhmmdnaufall.restful.model.UpdateContactRequest
import mhmmdnaufall.restful.repository.ContactRepository
import mhmmdnaufall.restful.service.ContactService
import mhmmdnaufall.restful.service.ValidationService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
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

        return toContactResponse(contact)
    }

    @Transactional(readOnly = true)
    override fun get(user: User, id: String): ContactResponse {
        val contact = contactRepository.findFirstByUserAndId(user, id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found")

        return toContactResponse(contact)
    }

    @Transactional
    override fun update(user: User, request: UpdateContactRequest): ContactResponse {
        validationService.validate(request)

        val contact = contactRepository.findFirstByUserAndId(user, request.id!!) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found")

        contact.apply {
            firstName = request.firstName
            lastName = request.lastName
            email = request.email
            phone = request.phone
        }

        contactRepository.save(contact)

        return toContactResponse(contact)
    }

    @Transactional
    override fun delete(user: User, contactId: String) {
        val contact = contactRepository.findFirstByUserAndId(user, contactId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found")

        contactRepository.delete(contact)
    }

    private fun toContactResponse(contact: Contact): ContactResponse {
        return ContactResponse(
                id = contact.id,
                firstName = contact.firstName,
                lastName = contact.lastName,
                email = contact.email,
                phone = contact.phone
        )
    }

}