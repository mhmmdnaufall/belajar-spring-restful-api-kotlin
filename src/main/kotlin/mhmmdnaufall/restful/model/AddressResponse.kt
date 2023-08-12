package mhmmdnaufall.restful.model

data class AddressResponse(

        val id: String,

        val street: String?,

        val city: String?,

        val province: String?,

        val country: String,

        val postalCode: String?

)
