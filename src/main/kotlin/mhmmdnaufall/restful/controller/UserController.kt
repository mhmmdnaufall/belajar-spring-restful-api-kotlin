package mhmmdnaufall.restful.controller

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.RegisterUserRequest
import mhmmdnaufall.restful.model.UpdateUserRequest
import mhmmdnaufall.restful.model.UserResponse
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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

    @GetMapping(
            path = ["/api/users/current"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun get(user: User): WebResponse<UserResponse> {
        val userResponse = userService.get(user)
        return WebResponse(data = userResponse)
    }

    @PatchMapping(
            path = ["/api/users/current"],
            consumes = [MediaType.APPLICATION_JSON_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun update(user: User, @RequestBody request: UpdateUserRequest): WebResponse<UserResponse> {
        val userResponse = userService.update(user, request)
        return WebResponse(data = userResponse)
    }

}