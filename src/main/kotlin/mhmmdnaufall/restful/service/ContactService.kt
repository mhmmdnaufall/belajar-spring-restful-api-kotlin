package mhmmdnaufall.restful.service

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.ContactResponse
import mhmmdnaufall.restful.model.CreateContactRequest
import mhmmdnaufall.restful.model.UpdateContactRequest

interface ContactService {

    fun create(user: User, request: CreateContactRequest): ContactResponse

    fun get(user: User, id: String): ContactResponse

    fun update(user: User, request: UpdateContactRequest): ContactResponse

}