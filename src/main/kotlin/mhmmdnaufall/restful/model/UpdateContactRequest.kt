package mhmmdnaufall.restful.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateContactRequest(

        @field:JsonIgnore // biar di-ignore sama si ObjectMapper
        @field:NotBlank
        var id: String? = null,

        @field:NotBlank
        @field:Size(max = 100)
        val firstName: String,

        @field:Size(max = 100)
        val lastName: String? = null,

        @field:Size(max = 100)
        @field:Email
        val email: String? = null,

        @field:Size(max = 100)
        @field:Pattern(
                regexp = "^\\+?\\d{1,3}[-.\\s]?\\(?\\d{1,3}\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}$",
                message = "phone: must be a well-formed phone number"
        )
        val phone: String? = null

)