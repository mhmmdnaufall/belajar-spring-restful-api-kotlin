package mhmmdnaufall.restful.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.ContactResponse
import mhmmdnaufall.restful.model.CreateContactRequest
import mhmmdnaufall.restful.model.WebResponse
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
import java.time.Instant

import org.springframework.test.web.servlet.MockMvcBuilder.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var contactRepository: ContactRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        contactRepository.deleteAll()
        userRepository.deleteAll()

        val user = User(
                username = "test",
                password = BCrypt.hashpw("test", BCrypt.gensalt()),
                name = "Test",
                token = "test",
                tokenExpiredAt = Instant.now().plusSeconds(10000000L).toEpochMilli()
        )
        userRepository.save(user)
    }

    @Test
    fun createContactBadRequest() {
        val request = CreateContactRequest(
                firstName = "",
                email = "salah"
        )

        mockMvc
                .perform(
                        post("/api/contacts")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header("X-API-TOKEN", "test")
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
    fun createContactSuccess() {
        val request = CreateContactRequest(
                firstName = "Muhammad",
                lastName = "Naufal",
                email = "naufal@gmail.com",
                phone = "088888888888"
        )

        mockMvc
                .perform(
                        post("/api/contacts")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<ContactResponse>>(){})

                    assertNull(response.errors)
                    assertEquals("Muhammad", response.data?.firstName)
                    assertEquals("Naufal", response.data?.lastName)
                    assertEquals("naufal@gmail.com", response.data?.email)
                    assertEquals("088888888888", response.data?.phone)

                    assertTrue(contactRepository.existsById(response.data?.id!!))
                }
    }

}