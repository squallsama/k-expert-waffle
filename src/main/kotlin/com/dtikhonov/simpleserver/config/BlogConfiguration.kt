package com.dtikhonov.simpleserver.config

import com.dtikhonov.simpleserver.extensions.logger
import com.dtikhonov.simpleserver.models.Article
import com.dtikhonov.simpleserver.models.Person
import com.dtikhonov.simpleserver.models.Role
import com.dtikhonov.simpleserver.models.RoleType
import com.dtikhonov.simpleserver.repositories.ArticleRepository
import com.dtikhonov.simpleserver.repositories.RoleRepository
import com.dtikhonov.simpleserver.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class BlogConfiguration {

    private val log = logger()

    @Autowired
    @Qualifier("passwordEncoderAndMatcher")
    private lateinit var passwordEncoder: PasswordEncoder

    @Value("\${credentials.admin.pass}")
    val adminPass: String? = null

    @Value("\${credentials.test-user.pass}")
    val testUserPass: String? = null

    @Bean
    fun databaseInitializer(userRepository: UserRepository,
                            roleRepository: RoleRepository,
                            articleRepository: ArticleRepository
    ) = ApplicationRunner {
        if (roleRepository.count() < RoleType.values().size) {
            log.warning("Empty roles, will create predefined roles")
            RoleType.values().forEach { roleType ->  roleRepository.save(Role(roleType = roleType))}
        }
        if (userRepository.count() == 0L) {
            val adminRole = roleRepository.findByRoleType(RoleType.ADMIN)
            val encodedPass = passwordEncoder.encode(adminPass)
            userRepository.save(Person(
                username = "admin", email = "admin@localhost.ru", password = encodedPass,
                passwordConfirm = encodedPass, roles = mutableSetOf(adminRole)))

            val userRole = roleRepository.findByRoleType(RoleType.USER)
            val encodedUserPass = passwordEncoder.encode(testUserPass)
            val testUser = userRepository.save(Person(
                username = "test-user", email = "test-user@localhost.ru", password = encodedUserPass,
                passwordConfirm = encodedUserPass, roles = mutableSetOf(userRole)
            ))
            
            if (articleRepository.count() == 0L) {
                articleRepository.save(
                    Article(
                        title = "Reactor Bismuth is out",
                        headline = "Lorem ipsum",
                        content = "dolor sit amet",
                        author = testUser
                    )
                )
                articleRepository.save(
                    Article(
                        title = "Reactor Aluminium has landed",
                        headline = "Lorem ipsum",
                        content = "dolor sit amet",
                        author = testUser
                    )
                )
            }
        }
    }
}