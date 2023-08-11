package mhmmdnaufall.restful.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mhmmdnaufall.restful.entity.Contact
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.ContactResponse
import mhmmdnaufall.restful.model.CreateContactRequest
import mhmmdnaufall.restful.model.UpdateContactRequest
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
import java.util.UUID

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
                firstName = "", // salah format
                email = "salah-format", // salah format
                phone = "salah-format" // salah format
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
                phone = "+6212345678901"
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
                    assertEquals("+6212345678901", response.data?.phone)

                    assertTrue(contactRepository.existsById(response.data?.id!!))
                }
    }

    @Test
    fun getContactNotFound() {
        mockMvc
                .perform(
                        get("/api/contacts/621736128")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isNotFound
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<String>>(){})
                    assertNotNull(response.errors)
                }
    }

    @Test
    fun getContactSuccess() {
        val user = userRepository.findById("test").orElseThrow()

        val contact = Contact(
                id = UUID.randomUUID().toString(),
                user = user,
                firstName = "Muhammad",
                lastName = "Naufal",
                email = "naufal@gmail.com",
                phone = "71289738921"
        )
        contactRepository.save(contact)

        mockMvc
                .perform(
                        get("/api/contacts/${contact.id}")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<ContactResponse>>(){})
                    assertNull(response.errors)

                    assertEquals(contact.id, response.data?.id)
                    assertEquals(contact.firstName, response.data?.firstName)
                    assertEquals(contact.lastName, response.data?.lastName)
                    assertEquals(contact.email, response.data?.email)
                    assertEquals(contact.phone, response.data?.phone)

                }
    }

    @Test
    fun updateContactBadRequest() {
        val request = UpdateContactRequest(
                firstName = "",
                email = "salah-format",
                phone = "salah-format"
        )

        mockMvc
                .perform(
                        put("/api/contacts/1234")
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
    fun updateContactSuccess() {

        val user = userRepository.findById("test").orElseThrow()

        val contact = Contact(
                id = UUID.randomUUID().toString(),
                user = user,
                firstName = "Muhammad",
                lastName = "Naufal",
                email = "naufal@gmail.com",
                phone = "+6212345678901"
        )
        contactRepository.save(contact)

        val request = CreateContactRequest(
                firstName = "Tom",
                lastName = "Delonge",
                email = "tom@gmail.com",
                phone = "+6212345678901"
        )

        mockMvc
                .perform(
                        put("/api/contacts/${contact.id}")
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
                    assertEquals(request.firstName, response.data?.firstName)
                    assertEquals(request.lastName, response.data?.lastName)
                    assertEquals(request.email, response.data?.email)
                    assertEquals(request.phone, response.data?.phone)

                    assertTrue(contactRepository.existsById(response.data?.id!!))

                }

    }
}