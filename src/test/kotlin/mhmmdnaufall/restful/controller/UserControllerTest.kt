package mhmmdnaufall.restful.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.RegisterUserRequest
import mhmmdnaufall.restful.model.UpdateUserRequest
import mhmmdnaufall.restful.model.UserResponse
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.repository.UserRepository
import mhmmdnaufall.restful.security.BCrypt
import org.hibernate.sql.Update
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

import org.springframework.test.web.servlet.MockMvcBuilder.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
    }

    @Test
    fun testRegisterSuccess() {

        val request = RegisterUserRequest(
                username = "test",
                password = "rahasia",
                name = "test"
        )

        mockMvc
                .perform(
                        post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<String>>(){})
                    assertEquals("OK", response.data)
                }

    }

    @Test
    fun testRegisterBadRequest() {

        val request = RegisterUserRequest(
                username = "",
                password = "",
                name = ""
        )

        mockMvc
                .perform(
                        post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isBadRequest
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<String>>(){})

                    assertNotNull(response.errors)
                }

    }

    @Test
    fun testRegisterDuplicate() {
        val user = User(
                username = "test",
                password = "rahasia",
                name = "test"
        )
        userRepository.save(user)

        val (username, password, name) = user

        val request = RegisterUserRequest(username, password, name)

        mockMvc
                .perform(
                        post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isBadRequest
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<String>>(){})

                    assertNotNull(response.errors)
                }
    }

    @Test
    fun getUserUnauthorized() {
        mockMvc
                .perform(
                        get("/api/users/current")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "notfound")
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
    fun getUserUnauthorizedTokenNotSend() {
        mockMvc
                .perform(
                        get("/api/users/current")
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
    fun getUserSuccess() {

        val user = User(
                username = "test",
                password = BCrypt.hashpw("rahasia", BCrypt.gensalt()),
                name = "Test",
                token = "test",
                tokenExpiredAt = Instant.now().plusSeconds(1000000L).toEpochMilli()
        )
        userRepository.save(user)

        mockMvc
                .perform(
                        get("/api/users/current")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<UserResponse>>(){})

                    assertNull(response.errors)
                    assertEquals("test", response.data?.username)
                    assertEquals("Test", response.data?.name)
                }
    }

    @Test
    fun getUserTokenExpired() {

        val user = User(
                username = "test",
                password = BCrypt.hashpw("rahasia", BCrypt.gensalt()),
                name = "Test",
                token = "test",
                tokenExpiredAt = Instant.now().minusSeconds(1000000L).toEpochMilli()
        )
        userRepository.save(user)

        mockMvc
                .perform(
                        get("/api/users/current")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "test")
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
    fun updateUserUnauthorized() {
        val request = UpdateUserRequest()

        mockMvc
                .perform(
                        patch("/api/users/current")
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
    fun updateUserSuccess() {
        val user = User(
                username = "test",
                password = BCrypt.hashpw("rahasia", BCrypt.gensalt()),
                name = "Test",
                token = "test",
                tokenExpiredAt = Instant.now().plusSeconds(100000L).toEpochMilli()
        )
        userRepository.save(user)

        val request = UpdateUserRequest(
                name = "Naufal",
                password = "naufalgantenkbgt"
        )

        mockMvc
                .perform(
                        patch("/api/users/current")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<UserResponse>>(){})

                    assertNull(response.errors)
                    assertEquals("Naufal", response.data?.name)
                    assertEquals("test", response.data?.username)

                    val userDb = userRepository.findById("test").orElse(null)
                    assertNotNull(userDb)
                    assertTrue(BCrypt.checkpw("naufalgantenkbgt", userDb.password))
                }
    }

}
