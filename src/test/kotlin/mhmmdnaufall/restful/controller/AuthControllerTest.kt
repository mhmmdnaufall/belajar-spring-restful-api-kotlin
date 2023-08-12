package mhmmdnaufall.restful.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.LoginUserRequest
import mhmmdnaufall.restful.model.TokenResponse
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.repository.AddressRepository
import mhmmdnaufall.restful.repository.ContactRepository
import mhmmdnaufall.restful.repository.UserRepository
import mhmmdnaufall.restful.security.BCrypt
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.junit.jupiter.api.Assertions.*

import org.springframework.test.web.servlet.MockMvcBuilder.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var contactRepository: ContactRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        addressRepository.deleteAll()
        contactRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun loginFailedUserNotFound() {

        val request = LoginUserRequest(
                username = "test",
                password = "test"
        )

        mockMvc
                .perform(
                        post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isUnauthorized
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<String>>(){})
                    assertNotNull(response.errors)
                }

    }

    @Test
    fun loginFailedWrongPassword() {
        val user = User(
                name = "Test",
                username = "test",
                password = BCrypt.hashpw("test", BCrypt.gensalt())
        )
        userRepository.save(user)

        val request = LoginUserRequest(
                username = "test",
                password = "salah"
        )

        mockMvc
                .perform(
                        post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isUnauthorized
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<String>>(){})
                    assertNotNull(response.errors)
                }
    }

    @Test
    fun loginSuccess() {
        val user = User(
                name = "Test",
                username = "test",
                password = BCrypt.hashpw("test", BCrypt.gensalt())
        )
        userRepository.save(user)

        val request = LoginUserRequest(
                username = "test",
                password = "test"
        )

        mockMvc
                .perform(
                        post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<TokenResponse>>(){})
                    assertNull(response.errors)
                    assertNotNull(response.data?.token)
                    assertNotNull(response.data?.expiredAt)

                    val userDb = userRepository.findById("test").orElse(null)
                    assertNotNull(userDb)
                    assertEquals(userDb.token, response.data?.token)
                    assertEquals(userDb.tokenExpiredAt, response.data?.expiredAt)
                }


    }

    @Test
    fun logoutFailed() {
        mockMvc
                .perform(
                        delete("/api/auth/logout")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isUnauthorized
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<String>>(){})
                    assertNotNull(response.errors)
                }
    }

    @Test
    fun logoutSuccess() {

        val user = User(
                username = "test",
                name = "Test",
                password = BCrypt.hashpw("test", BCrypt.gensalt()),
                token = "test",
                tokenExpiredAt = Instant.now().plusSeconds(1000000L).toEpochMilli()
        )
        userRepository.save(user)

        mockMvc
                .perform(
                        delete("/api/auth/logout")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<String>>(){})

                    assertNull(response.errors)
                    assertEquals("OK", response.data)

                    val userDb = userRepository.findById("test").orElse(null)
                    assertNotNull(userDb)
                    assertNull(userDb.tokenExpiredAt)
                    assertNull(userDb.token)

                }

    }
}