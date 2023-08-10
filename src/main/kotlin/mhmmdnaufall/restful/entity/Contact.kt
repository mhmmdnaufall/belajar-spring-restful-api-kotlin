package mhmmdnaufall.restful.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "contacts")
data class Contact(

        @field:Id
        val id: String,

        @field:Column(name = "first_name")
        var firstName: String,

        @field:Column(name = "last_name")
        var lastName: String,

        var phone: String,

        var email: String,

        @field:ManyToOne
        @field:JoinColumn(name = "username", referencedColumnName = "username")
        var user: User,

        @field:OneToMany(mappedBy = "contact")
        var addresses: List<Address>

)
