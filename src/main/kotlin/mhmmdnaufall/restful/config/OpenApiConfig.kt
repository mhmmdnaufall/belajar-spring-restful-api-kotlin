package mhmmdnaufall.restful.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
        info = Info(
                contact = Contact(
                        name = "Muhammad Naufal",
                        email = "muhammadnaufaall@gmail.com",
                        url = "https://github.com/mhmmdnaufall"
                ),
                description = "OpenAPI documentation for Spring RESTful API with Kotlin",
                title = "OpenAPI specification - mhmmdnaufall",
                version = "1.0"
        ),
        servers = [Server(
                description = "Local ENV",
                url = "http://localhost:8080"
        )]
)
class OpenApiConfig {
}