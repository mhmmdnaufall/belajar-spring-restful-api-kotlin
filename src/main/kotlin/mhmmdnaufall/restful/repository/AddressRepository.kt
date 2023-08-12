package mhmmdnaufall.restful.repository

import mhmmdnaufall.restful.entity.Address
import mhmmdnaufall.restful.entity.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : JpaRepository<Address, String> {

    fun findFirstByContactAndId(contact: Contact, id: String): Address?

    fun findAllByContact(contact: Contact): List<Address>

}