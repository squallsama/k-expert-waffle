package com.dtikhonov.simpleserver

import com.dtikhonov.simpleserver.models.Article
import com.dtikhonov.simpleserver.models.Person
import com.dtikhonov.simpleserver.models.Role
import com.dtikhonov.simpleserver.models.RoleType
import com.dtikhonov.simpleserver.repositories.ArticleRepository
import com.dtikhonov.simpleserver.repositories.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpControllersTests {

    @Autowired
    private lateinit var context: WebApplicationContext
    
    lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setup() {
        println(">> Setup")
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @MockkBean
    private lateinit var userRepository: UserRepository

    @MockkBean
    private lateinit var articleRepository: ArticleRepository

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `List articles`() {
        val userRole = Role(roleType = RoleType.USER)
        val testUser = Person(username = "springjuergen", password = "Juergen", passwordConfirm = "Juergen",
            email = "admin@localhost.ru", roles = mutableSetOf(userRole))
        val spring5Article = Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Lorem ipsum", testUser)
        val spring43Article = Article("Spring Framework 4.3 goes GA", "Dear Spring community ...", "Lorem ipsum", testUser)
        every { articleRepository.findAllByOrderByAddedAtDesc() } returns listOf(spring5Article, spring43Article)
        every { userRepository.save(any<Person>())} returns testUser
        mockMvc.perform(get("/api/article/").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("\$.[0].author.login").value(testUser.username))
            .andExpect(jsonPath("\$.[1].author.login").value(testUser.username))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `List users`() {
        val userRole = Role(roleType = RoleType.USER)
        val testUser1 = Person(username = "springjuergen", password = "Juergen", passwordConfirm = "pass1",
            email = "springjuergen@localhost.ru", roles = mutableSetOf(userRole))
        val testUser2 = Person(username = "Stéphane", password = "Juergen", passwordConfirm = "pass2",
            email = "smaldini@localhost.ru", roles = mutableSetOf(userRole))
        every { userRepository.findAll() } returns listOf(testUser1, testUser2)
        mockMvc.perform(get("/api/user/").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("\$.[0].login").value(testUser1.username))
            .andExpect(jsonPath("\$.[1].login").value(testUser2.username))
    }
}