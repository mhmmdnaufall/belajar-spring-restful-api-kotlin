package mhmmdnaufall.restful.service

import mhmmdnaufall.restful.model.LoginUserRequest
import mhmmdnaufall.restful.model.TokenResponse

interface AuthService {

    fun login(request: LoginUserRequest): TokenResponse

}