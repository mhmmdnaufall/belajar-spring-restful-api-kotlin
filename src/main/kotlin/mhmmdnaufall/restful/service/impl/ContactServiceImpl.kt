package mhmmdnaufall.restful.service.impl

import jakarta.persistence.criteria.Predicate
import mhmmdnaufall.restful.entity.Contact
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.ContactResponse
import mhmmdnaufall.restful.model.CreateContactRequest
import mhmmdnaufall.restful.model.SearchContactRequest
import mhmmdnaufall.restful.model.UpdateContactRequest
import mhmmdnaufall.restful.repository.ContactRepository
import mhmmdnaufall.restful.service.ContactService
import mhmmdnaufall.restful.service.ValidationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

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
        val contact = contactRepository.findFirstByUserAndId(user, id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, CONTACT_NOT_FOUND_MESSAGE)

        return toContactResponse(contact)
    }

    @Transactional
    override fun update(user: User, request: UpdateContactRequest): ContactResponse {
        validationService.validate(request)

        val contact = contactRepository.findFirstByUserAndId(user, request.id!!)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, CONTACT_NOT_FOUND_MESSAGE)

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
        val contact = contactRepository.findFirstByUserAndId(user, contactId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, CONTACT_NOT_FOUND_MESSAGE)

        contactRepository.delete(contact)
    }

    @Transactional(readOnly = true)
    override fun search(user: User, request: SearchContactRequest): Page<ContactResponse> {
        val specification = Specification<Contact> { root, query, builder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(builder.equal(root.get<User>("user"), user))
            if (request.name != null) {
                predicates.add(builder.or(
                        builder.like(root["firstName"], "%${request.name}%"),
                        builder.like(root["lastName"], "%${request.name}%")
                ))
            }
            if (request.email != null) {
                predicates.add(builder.like(root["email"], "%${request.email}%"))
            }
            if (request.phone != null) {
                predicates.add(builder.like(root["phone"], "%${request.phone}%"))
            }

            query.where(*predicates.toTypedArray()).restriction
        }

        val pageable: Pageable = PageRequest.of(request.page, request.size)
        val contacts: Page<Contact> = contactRepository.findAll(specification, pageable)

        val contactResponses: List<ContactResponse> = contacts.content.map(::toContactResponse)

        return PageImpl(contactResponses, pageable, contacts.totalElements)
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

    companion object {
        const val CONTACT_NOT_FOUND_MESSAGE = "Contact not found"
    }

}