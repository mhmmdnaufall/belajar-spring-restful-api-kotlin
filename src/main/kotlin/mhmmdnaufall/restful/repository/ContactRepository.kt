package mhmmdnaufall.restful.repository

import mhmmdnaufall.restful.entity.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, String> { }