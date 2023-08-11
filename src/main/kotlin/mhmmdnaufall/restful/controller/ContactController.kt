package mhmmdnaufall.restful.controller

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.ContactResponse
import mhmmdnaufall.restful.model.CreateContactRequest
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.service.ContactService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
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

}