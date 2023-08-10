package mhmmdnaufall.restful.service

import mhmmdnaufall.restful.model.RegisterUserRequest

interface UserService {

    fun register(request: RegisterUserRequest)

}