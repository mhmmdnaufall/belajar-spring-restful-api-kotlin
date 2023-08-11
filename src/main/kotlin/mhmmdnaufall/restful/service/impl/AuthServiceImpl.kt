package mhmmdnaufall.restful.service.impl

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.LoginUserRequest
import mhmmdnaufall.restful.model.TokenResponse
import mhmmdnaufall.restful.repository.UserRepository
import mhmmdnaufall.restful.security.BCrypt
import mhmmdnaufall.restful.service.AuthService
import mhmmdnaufall.restful.service.ValidationService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.UUID

@Service
class AuthServiceImpl(
        private val userRepository: UserRepository,
        private val validationService: ValidationService
) : AuthService {

    @Transactional
    override fun login(request: LoginUserRequest): TokenResponse {

        validationService.validate(request)

        val user = userRepository.findById(request.username).orElseThrow {
            ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong")
            // jangan ngasih tau yang mana yang salah, takutnya nanti dicoba-coba
        }

        return if (BCrypt.checkpw(request.password, user.password)) {
            user.token = UUID.randomUUID().toString()
            user.tokenExpiredAt = next30Days()
            userRepository.save(user)

            TokenResponse(
                    token = user.token!!,
                    expiredAt = user.tokenExpiredAt!!
            )
        } else {
            // gagal
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong")
        }

    }

    @Transactional
    override fun logout(user: User) {
        user.apply {
            token = null
            tokenExpiredAt = null
        }

        userRepository.save(user)
    }

    private fun next30Days(): Long = Instant.now().plusSeconds(60 * 60 * 24 * 30).toEpochMilli()

}