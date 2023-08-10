package mhmmdnaufall.restful.service

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.RegisterUserRequest
import mhmmdnaufall.restful.model.UserResponse

interface UserService {

    fun register(request: RegisterUserRequest)

    fun get(user: User): UserResponse

}