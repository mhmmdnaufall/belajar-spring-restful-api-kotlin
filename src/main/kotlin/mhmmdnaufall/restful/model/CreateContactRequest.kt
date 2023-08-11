package mhmmdnaufall.restful.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateContactRequest(

        @field:NotBlank
        @field:Size(max = 100)
        val firstName: String,

        @field:Size(max = 100)
        val lastName: String? = null,

        @field:Size(max = 100)
        @field:Email
        val email: String? = null,

        @field:Size(max = 100)
        val phone: String? = null

)