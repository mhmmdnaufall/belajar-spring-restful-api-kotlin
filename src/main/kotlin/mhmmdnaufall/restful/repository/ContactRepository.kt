package mhmmdnaufall.restful.repository

import mhmmdnaufall.restful.entity.Contact
import mhmmdnaufall.restful.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact> {

    fun findFirstByUserAndId(user: User, id: String): Contact?

}