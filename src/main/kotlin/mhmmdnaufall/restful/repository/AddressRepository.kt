package mhmmdnaufall.restful.repository

import mhmmdnaufall.restful.entity.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : JpaRepository<Address, String> { }