package mhmmdnaufall.restful.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mhmmdnaufall.restful.entity.Address
import mhmmdnaufall.restful.entity.Contact
import mhmmdnaufall.restful.entity.User
import mhmmdnaufall.restful.model.AddressResponse
import mhmmdnaufall.restful.model.CreateAddressRequest
import mhmmdnaufall.restful.model.UpdateAddressRequest
import mhmmdnaufall.restful.model.WebResponse
import mhmmdnaufall.restful.repository.AddressRepository
import mhmmdnaufall.restful.repository.ContactRepository
import mhmmdnaufall.restful.repository.UserRepository
import mhmmdnaufall.restful.security.BCrypt
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import java.time.Instant

import org.springframework.test.web.servlet.MockMvcBuilder.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

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

        val user = User(
                username = "test",
                password = BCrypt.hashpw("test", BCrypt.gensalt()),
                name = "Test",
                token = "test",
                tokenExpiredAt = Instant.now().plusSeconds(10000000L).toEpochMilli()
        )
        userRepository.save(user)

        val contact = Contact(
                id = "test",
                user = user,
                firstName = "Muhammad",
                lastName = "Naufal",
                email = "naufal@gmail.com",
                phone = "+621234567890"
        )
        contactRepository.save(contact)
    }

    @Test
    fun createAddressBadRequest() {
        val request = CreateAddressRequest(
                country = ""
        )

        mockMvc
                .perform(
                        post("/api/contacts/test/addresses")
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
    fun createAddressSuccess() {
        val request = CreateAddressRequest(
                street = "Jalan",
                city = "Jakarta",
                province = "DKI",
                country = "Indonesia",
                postalCode = "123123"
        )

        mockMvc
                .perform(
                        post("/api/contacts/test/addresses")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<AddressResponse>>(){})

                    assertNull(response.errors)
                    assertEquals(request.street, response.data?.street)
                    assertEquals(request.city, response.data?.city)
                    assertEquals(request.province, response.data?.province)
                    assertEquals(request.country, response.data?.country)
                    assertEquals(request.postalCode, response.data?.postalCode)

                    assertTrue(addressRepository.existsById(response.data?.id!!))
                }
    }

    @Test
    fun getAddressNotFound() {

        mockMvc
                .perform(
                        get("/api/contacts/test/addresses/test")
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
    fun getAddressSuccess() {
        val contact = contactRepository.findById("test").orElseThrow()

        val address = Address(
                id = "test",
                contact = contact,
                street = "Jalan",
                city = "Jakarta",
                province = "DKI",
                country = "Indonesia",
                postalCode = "123123"
        )
        addressRepository.save(address)

        mockMvc
                .perform(
                        get("/api/contacts/test/addresses/test")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<AddressResponse>>(){})

                    assertNull(response.errors)
                    assertEquals(address.id, response.data?.id)
                    assertEquals(address.street, response.data?.street)
                    assertEquals(address.city, response.data?.city)
                    assertEquals(address.province, response.data?.province)
                    assertEquals(address.country, response.data?.country)
                    assertEquals(address.postalCode, response.data?.postalCode)
                }
    }

    @Test
    fun updateAddressBadRequest() {
        val request = UpdateAddressRequest(
                country = ""
        )

        mockMvc
                .perform(
                        put("/api/contacts/test/addresses/test")
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
    fun updateAddressSuccess() {

        val contact = contactRepository.findById("test").orElseThrow()

        val address = Address(
                id = "test",
                contact = contact,
                street = "Lama",
                city = "Lama",
                province = "Lava",
                country = "Lama",
                postalCode = "77897907"
        )
        addressRepository.save(address)

        val request = UpdateAddressRequest(
                street = "Jalan",
                city = "Jakarta",
                province = "DKI",
                country = "Indonesia",
                postalCode = "123123"
        )

        mockMvc
                .perform(
                        put("/api/contacts/test/addresses/test")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header("X-API-TOKEN", "test")
                )
                .andExpectAll(
                        status().isOk
                )
                .andDo { result ->
                    val response = objectMapper.readValue(result.response.contentAsString, object : TypeReference<WebResponse<AddressResponse>>(){})

                    assertNull(response.errors)
                    assertEquals(request.street, response.data?.street)
                    assertEquals(request.city, response.data?.city)
                    assertEquals(request.province, response.data?.province)
                    assertEquals(request.country, response.data?.country)
                    assertEquals(request.postalCode, response.data?.postalCode)

                    assertTrue(addressRepository.existsById(response.data?.id!!))
                }

    }

    @Test
    fun deleteAddressNotFound() {
        mockMvc
                .perform(
                        delete("/api/contacts/test/addresses/test")
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
    fun deleteAddressSuccess() {

        val contact = contactRepository.findById("test").orElseThrow()

        val address = Address(
                id = "test",
                contact = contact,
                street = "Jalan",
                city = "Jakarta",
                province = "DKI",
                country = "Indonesia",
                postalCode = "123123"
        )
        addressRepository.save(address)

        mockMvc
                .perform(
                        delete("/api/contacts/test/addresses/test")
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

                    assertFalse(addressRepository.existsById("test"))
                }

    }
}