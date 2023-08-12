package mhmmdnaufall.restful.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateAddressRequest(

        @field:JsonIgnore // biar di-ignore sama si ObjectMapper
        @field:NotBlank
        var contactId: String? = null,

        @field:Size(max = 200)
        val street: String? = null,

        @field:Size(max = 100)
        val city: String? = null,

        @field:Size(max = 100)
        val province: String? = null,

        @field:NotBlank
        @field:Size(max = 100)
        val country: String,

        @field:Size(max = 10)
        val postalCode: String? = null

)
