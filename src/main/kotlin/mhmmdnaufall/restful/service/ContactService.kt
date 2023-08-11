package mhmmdnaufall.restful.service

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.ContactResponse
import mhmmdnaufall.restful.model.CreateContactRequest

interface ContactService {

    fun create(user: User, request: CreateContactRequest): ContactResponse

}