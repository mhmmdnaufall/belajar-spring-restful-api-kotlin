package mhmmdnaufall.restful.controller

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.LoginUserRequest
import mhmmdnaufall.restful.model.TokenResponse
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.service.AuthService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
        private val authService: AuthService
) {

    @PostMapping(
            path = ["/api/auth/login"],
            consumes = [MediaType.APPLICATION_JSON_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun login(@RequestBody request: LoginUserRequest): WebResponse<TokenResponse> {
        val tokenResponse = authService.login(request)
        return WebResponse(data = tokenResponse)
    }

    @DeleteMapping(
            path = ["/api/auth/logout"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun logout(user: User): WebResponse<String> {
        authService.logout(user)
        return WebResponse(data = "OK")
    }

}