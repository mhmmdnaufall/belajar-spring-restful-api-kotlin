package mhmmdnaufall.restful.controller

import mhmmdnaufall.restful.model.RegisterUserRequest
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {

    @PostMapping(
            path = ["/api/users"],
            consumes = [MediaType.APPLICATION_JSON_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun register(@RequestBody request: RegisterUserRequest): WebResponse<String> {
        userService.register(request)
        return WebResponse(data = "OK")
    }

}