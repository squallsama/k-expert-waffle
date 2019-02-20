package com.dtikhonov.simpleserver.repositories

import com.dtikhonov.simpleserver.models.Article
import com.dtikhonov.simpleserver.models.User
import org.springframework.data.repository.CrudRepository

interface ArticleRepository : CrudRepository<Article, Long> {
    fun findBySlug(slug: String): Article?
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>
}

interface UserRepository : CrudRepository<User, Long> {
    fun findByLogin(login: String): User
}