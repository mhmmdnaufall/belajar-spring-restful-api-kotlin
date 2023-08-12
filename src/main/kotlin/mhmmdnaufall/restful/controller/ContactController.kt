package mhmmdnaufall.restful.controller

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.*
import mhmmdnaufall.restful.service.ContactService
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
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

    @DeleteMapping(
            path = ["/api/contacts/{contactId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun delete(user: User, @PathVariable("contactId") contactId: String): WebResponse<String> {
        contactService.delete(user, contactId)
        return WebResponse(data = "OK")
    }

    @GetMapping(
            path = ["/api/contacts"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun search(
            user: User,
            @RequestParam(value = "name", required = false) name: String?,
            @RequestParam(value = "email", required = false) email: String?,
            @RequestParam(value = "phone", required = false) phone: String?,
            @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
            @RequestParam(value = "size", required = false, defaultValue = "10") size: Int
    ): WebResponse<List<ContactResponse>> {

        val request = SearchContactRequest(
                name = name,
                email = email,
                phone = phone,
                page = page,
                size = size
        )

        val contactResponses: Page<ContactResponse> = contactService.search(user, request)

        return WebResponse(
                data = contactResponses.content,
                paging = PagingResponse(
                        currentPage = contactResponses.number,
                        totalPage = contactResponses.totalPages,
                        size = contactResponses.size,
                )
        )

    }

}