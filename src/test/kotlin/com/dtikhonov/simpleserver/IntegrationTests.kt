package com.dtikhonov.simpleserver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.*

import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import org.springframework.beans.factory.annotation.Value


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class IntegrationTests {

    @Autowired
    lateinit var context: WebApplicationContext
    lateinit var mvc: MockMvc

    @Value("\${blog.banner.title}")
    lateinit var blogTitle: String

    @BeforeAll
    fun setup() {
        println(">> Setup")
        mvc = MockMvcBuilders
            .webAppContextSetup(this.context)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    fun contextLoads() {
    }

    @Test
    @WithMockUser("USER")
    fun adminList_returnsAListOfAllItemsForAdmin() {
        val mvcResult = mvc.perform(get("/").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk)
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andReturn()

        val html = Jsoup.parse(mvcResult.getResponse().getContentAsString())

        val bannerTitle = html.select("h2[class=banner-title]").first()
        assertThat((bannerTitle.childNode(0) as TextNode).text()).isEqualTo(blogTitle)
    }

    @Test
    @WithMockUser(username = "test-user", authorities = ["USER"])
    fun auth_articles() {
        val testArticleTitle = "reactor-aluminium-has-landed"
        val mvcResult = mvc.perform(
            get("/article/$testArticleTitle")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

        val html = Jsoup.parse(mvcResult.getResponse().getContentAsString())

        val articleBody = html.select("div[class=article-description]").first()
        assertThat((articleBody.childNode(0) as TextNode).text()).contains("dolor sit amet")

    }

    @AfterAll
    fun teardown() {
        println(">> Tear down")
    }

}