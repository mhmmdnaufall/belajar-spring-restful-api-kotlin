package mhmmdnaufall.restful.model

import org.jetbrains.annotations.NotNull

data class SearchContactRequest(

        val name: String? = null,

        val email: String? = null,

        val phone: String? = null,

        @field:NotNull
        val page: Int,

        @field:NotNull
        val size: Int

)