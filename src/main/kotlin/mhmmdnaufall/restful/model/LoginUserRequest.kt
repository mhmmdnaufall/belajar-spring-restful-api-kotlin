package mhmmdnaufall.restful.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginUserRequest(

        @field:NotBlank
        @field:Size(max = 100)
        val username: String,

        @field:NotBlank
        @field:Size(max = 100)
        val password: String

)
