package mhmmdnaufall.restful.model

data class WebResponse<T>(

        val data: T? = null,

        val errors: String? = null,

        val paging: PagingResponse? = null

)