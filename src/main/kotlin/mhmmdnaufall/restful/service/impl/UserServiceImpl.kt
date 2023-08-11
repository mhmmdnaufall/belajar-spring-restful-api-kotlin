package mhmmdnaufall.restful.service.impl

import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.RegisterUserRequest
import mhmmdnaufall.restful.model.UpdateUserRequest
import mhmmdnaufall.restful.model.UserResponse
import mhmmdnaufall.restful.repository.UserRepository
import mhmmdnaufall.restful.security.BCrypt
import mhmmdnaufall.restful.service.UserService
import mhmmdnaufall.restful.service.ValidationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val validationService: ValidationService
) : UserService {

    private val log: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Transactional
    override fun register(request: RegisterUserRequest) {
        validationService.validate(request)

        if (userRepository.existsById(request.username))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered")


        val user = User(
                username = request.username,
                password = BCrypt.hashpw(request.password, BCrypt.gensalt()),
                name = request.name
        )

        userRepository.save(user)

    }

    override fun get(user: User): UserResponse {
        return UserResponse(
                username = user.username,
                name = user.name
        )
    }

    @Transactional
    override fun update(user: User, request: UpdateUserRequest): UserResponse {
        validationService.validate(request)

        if (request.name != null) {
            user.name = request.name
        }

        if (request.password != null) {
            user.password = BCrypt.hashpw(request.password, BCrypt.gensalt())
        }

        userRepository.save(user)

        log.info("USER : {}", user.name)

        return UserResponse(
                name = user.name,
                username = user.username
        )

    }
}