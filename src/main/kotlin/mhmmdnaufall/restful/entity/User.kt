package mhmmdnaufall.restful.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(

        @field:Id
        val username: String,

        var password: String,

        var name: String,

        var token: String? = null,

        @field:Column(name = "token_expired_at")
        var tokenExpiredAt: Long? = null,

        @field:OneToMany(mappedBy = "user")
        var contacts: List<Contact>? = null

)