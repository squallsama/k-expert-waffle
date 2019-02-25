package com.dtikhonov.simpleserver.config

import com.dtikhonov.simpleserver.models.Article
import com.dtikhonov.simpleserver.models.Person
import com.dtikhonov.simpleserver.models.Role
import com.dtikhonov.simpleserver.models.RoleType
import com.dtikhonov.simpleserver.repositories.ArticleRepository
import com.dtikhonov.simpleserver.repositories.RoleRepository
import com.dtikhonov.simpleserver.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class BlogConfiguration {

    @Autowired
    @Qualifier("passwordEncoderAndMatcher")
    private lateinit var passwordEncoder: PasswordEncoder

    @Bean
    fun databaseInitializer(userRepository: UserRepository,
                            roleRepository: RoleRepository,
                            articleRepository: ArticleRepository
    ) = ApplicationRunner {
        val adminRole = roleRepository.save(Role(roleType = RoleType.ADMIN))
        roleRepository.save(adminRole)
        val adminPass = "qwerty"
        val encodedPass = passwordEncoder.encode(adminPass)
        //init db if not exist
        val adminPerson = Person(username = "admin", email = "admin@localhost.ru", password = encodedPass,
            passwordConfirm = encodedPass, roles = mutableSetOf(adminRole))
        val adminUser = userRepository.save(adminPerson)
        articleRepository.save(Article(
            title = "Reactor Bismuth is out",
            headline = "Lorem ipsum",
            content = "dolor sit amet",
            author = adminUser
        )
        )
        articleRepository.save(Article(
            title = "Reactor Aluminium has landed",
            headline = "Lorem ipsum",
            content = "dolor sit amet",
            author = adminUser
        ))
    }
}