package mhmmdnaufall.restful.service.impl

import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import mhmmdnaufall.restful.service.ValidationService
import org.springframework.stereotype.Service

@Service
class ValidationServiceImpl(
        private val validator: Validator
) : ValidationService {

    override fun validate(request: Any) {

        val constraintViolation: Set<ConstraintViolation<Any>> = validator.validate(request)

        if (constraintViolation.isNotEmpty())
            throw ConstraintViolationException(constraintViolation)

    }

}