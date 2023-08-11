package mhmmdnaufall.restful.service

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.ContactResponse
import mhmmdnaufall.restful.model.CreateContactRequest
import mhmmdnaufall.restful.model.SearchContactRequest
import mhmmdnaufall.restful.model.UpdateContactRequest
import org.springframework.data.domain.Page

interface ContactService {

    fun create(user: User, request: CreateContactRequest): ContactResponse

    fun get(user: User, id: String): ContactResponse

    fun update(user: User, request: UpdateContactRequest): ContactResponse

    fun delete(user: User, contactId: String)

    fun search(user: User, request: SearchContactRequest): Page<ContactResponse>

}