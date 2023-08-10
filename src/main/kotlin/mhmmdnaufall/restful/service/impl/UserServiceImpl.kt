package mhmmdnaufall.restful.service.impl

import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.RegisterUserRequest
import mhmmdnaufall.restful.repository.UserRepository
import mhmmdnaufall.restful.security.BCrypt
import mhmmdnaufall.restful.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val validator: Validator
) : UserService {

    @Transactional
    override fun register(request: RegisterUserRequest) {
        val constraintViolations: Set<ConstraintViolation<RegisterUserRequest>> = validator.validate(request)

        if (constraintViolations.isNotEmpty())
            throw ConstraintViolationException(constraintViolations)

        if (userRepository.existsById(request.username))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered")


        val user = User(
                username = request.username,
                password = BCrypt.hashpw(request.password, BCrypt.gensalt()),
                name = request.name
        )

        userRepository.save(user)

    }


}