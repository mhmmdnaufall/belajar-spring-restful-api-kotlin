package mhmmdnaufall.restful.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "addresses")
data class Address(

        @field:Id
        val id: String,

        var street: String,

        var city: String,

        var province: String,

        var country: String,

        @field:Column(name = "postal_code")
        var postalCode: String,

        @field:ManyToOne
        @JoinColumn(name = "contact_id", referencedColumnName = "id")
        var contact: Contact

)