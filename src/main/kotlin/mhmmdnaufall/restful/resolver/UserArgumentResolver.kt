package mhmmdnaufall.restful.resolver

import jakarta.servlet.http.HttpServletRequest
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Component
class UserArgumentResolver(
        private val userRepository: UserRepository,
        private val log: Logger = LoggerFactory.getLogger(UserArgumentResolver::class.java)
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return User::class.java == parameter.parameterType
    }

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?
    ): Any? {

        val servletRequest = webRequest.nativeRequest as HttpServletRequest
        val token = servletRequest.getHeader("X-API-TOKEN")
        log.info("TOKEN {}", token)

        if (token == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
        }

        val user = userRepository.findFirstByToken(token) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")

        log.info("USER {}", user)
        if (user.tokenExpiredAt!! < Instant.now().toEpochMilli()) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
        }

        return user

    }

}