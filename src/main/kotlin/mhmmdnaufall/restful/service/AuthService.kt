package mhmmdnaufall.restful.service

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.LoginUserRequest
import mhmmdnaufall.restful.model.TokenResponse

interface AuthService {

    fun login(request: LoginUserRequest): TokenResponse

    fun logout(user: User)

}