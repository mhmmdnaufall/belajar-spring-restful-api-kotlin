package mhmmdnaufall.restful.repository

import mhmmdnaufall.restful.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String> {

    fun findFirstByToken(token: String): User?

}