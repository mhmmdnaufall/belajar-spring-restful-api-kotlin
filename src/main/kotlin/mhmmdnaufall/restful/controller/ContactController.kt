package mhmmdnaufall.restful.controller

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.*
import mhmmdnaufall.restful.service.ContactService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ContactController(private val contactService: ContactService) {

    @PostMapping(
            path = ["/api/contacts"],
            consumes = [MediaType.APPLICATION_JSON_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(user: User, @RequestBody request: CreateContactRequest): WebResponse<ContactResponse> {
        val contactResponse = contactService.create(user, request)
        return WebResponse(data = contactResponse)
    }

    @GetMapping(
            path = ["api/contacts/{contactId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun get(user: User, @PathVariable("contactId") contactId: String): WebResponse<ContactResponse> {
        val contactResponse = contactService.get(user, contactId)
        return WebResponse(data = contactResponse)
    }

    @PutMapping(
            path = ["/api/contacts/{contactId}"],
            consumes = [MediaType.APPLICATION_JSON_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun update(
            user: User,
            @RequestBody request: UpdateContactRequest,
            @PathVariable("contactId") contactId: String
    ): WebResponse<ContactResponse> {

        request.id = contactId

        val contactResponse = contactService.update(user, request)

        return WebResponse(data = contactResponse)

    }

}