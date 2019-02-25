package com.dtikhonov.simpleserver

import com.dtikhonov.simpleserver.models.*
import com.dtikhonov.simpleserver.repositories.ArticleRepository
import com.dtikhonov.simpleserver.repositories.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
    val articleRepository: ArticleRepository
) {

    @Test
    fun `When findByIdOrNull then return Article`() {
        val userRole = Role(roleType = RoleType.USER)
        entityManager.persistAndFlush(userRole)
        val juergen = Person(username = "springjuergen", password = "Juergen", passwordConfirm = "Juergen",
            email = "admin@localhost.ru", roles = mutableSetOf(userRole))
        entityManager.persistAndFlush(juergen)
        val article = Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Lorem ipsum", juergen)
        entityManager.persistAndFlush(article)
        val found = articleRepository.findByIdOrNull(article.id!!)
        assertThat(found).isEqualTo(article)
    }

    @Test
    fun `When findByLogin then return User`() {
        val userRole = Role(roleType = RoleType.USER)
        entityManager.persistAndFlush(userRole)
        val userRoles = mutableSetOf(userRole)
        val testUser = Person(username = "testName", password = "pass", passwordConfirm = "pass",
            email = "admin@localhost.ru", roles = userRoles)
        entityManager.persistAndFlush(testUser)
        val user = userRepository.findByUsername(testUser.username)
        assertThat(user).isEqualTo(testUser)
        assertThat(user.roles).isEqualTo(userRoles)
    }
}