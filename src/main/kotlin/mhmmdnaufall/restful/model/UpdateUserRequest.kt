package mhmmdnaufall.restful.model

import jakarta.validation.constraints.Size

data class UpdateUserRequest(

        @field:Size(max = 100)
        val name: String? = null,

        @field:Size(max = 100)
        val password: String? = null

)
