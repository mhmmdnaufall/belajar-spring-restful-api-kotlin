package mhmmdnaufall.restful.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.RegisterUserRequest
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.repository.UserRepository
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
}
