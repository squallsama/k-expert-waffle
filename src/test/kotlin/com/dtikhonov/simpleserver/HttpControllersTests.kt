package com.dtikhonov.simpleserver

import com.dtikhonov.simpleserver.models.Article
import com.dtikhonov.simpleserver.models.Person
import com.dtikhonov.simpleserver.models.RoleType
import com.dtikhonov.simpleserver.repositories.ArticleRepository
import com.dtikhonov.simpleserver.repositories.RoleRepository
import com.dtikhonov.simpleserver.repositories.UserRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
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


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpControllersTests {

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setup() {

        println(">> Setup")
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @Test
    @WithMockUser(username = "admin", authorities = ["ADMIN"])
    fun `List articles`() {
        val userRole = roleRepository.findByRoleType(RoleType.USER)
        val testUser = userRepository.save(
            Person(
                username = "springjuergen", password = "Juergen", passwordConfirm = "Juergen",
                email = "Juergen@localhost.ru", roles = mutableSetOf(userRole)
            )
        )
        val spring5Article =
            Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Lorem ipsum", testUser)
        val spring43Article =
            Article("Spring Framework 4.3 goes GA", "Dear Spring community ...", "Lorem ipsum", testUser)
        articleRepository.save(spring5Article)
        articleRepository.save(spring43Article)
        mockMvc.perform(get("/api/article/").accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.[0].author.username").value(testUser.username))
            .andExpect(jsonPath("\$.[1].author.username").value(testUser.username))
    }

    @Test
    @WithMockUser(username = "admin", authorities = ["ADMIN"])
    fun `List users`() {
        val userRole = roleRepository.findByRoleType(RoleType.USER)
        val testUser1 = Person(
            username = "springjuergen2", password = "Juergen", passwordConfirm = "pass1",
            email = "springjuergen2@localhost.ru", roles = mutableSetOf(userRole)
        )
        val testUser2 = Person(
            username = "Stéphane2", password = "Juergen", passwordConfirm = "pass2",
            email = "smaldini2@localhost.ru", roles = mutableSetOf(userRole)
        )
        userRepository.save(testUser1)
        userRepository.save(testUser2)
        mockMvc.perform(get("/api/user/").accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk)
                //check that last user equals to testUser2
            .andExpect(jsonPath("\$.[-1].username").value(testUser2.username))
            //check that user before last user equals to testUser1
            .andExpect(jsonPath("\$.[-2].username").value(testUser1.username))
    }
}