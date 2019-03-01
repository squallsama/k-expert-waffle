package com.dtikhonov.simpleserver.repositories

import com.dtikhonov.simpleserver.models.Article
import com.dtikhonov.simpleserver.models.Role
import com.dtikhonov.simpleserver.models.Person
import com.dtikhonov.simpleserver.models.RoleType
import org.springframework.stereotype.Repository
import org.springframework.data.repository.CrudRepository


@Repository
interface ArticleRepository : CrudRepository<Article, Long> {
    fun findBySlug(slug: String): Article?
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>
}

@Repository
interface UserRepository : CrudRepository<Person, Long> {
    fun findByUsername(username: String): Person
    fun findByEmail(email: String): Person
}

@Repository
interface RoleRepository : CrudRepository<Role, Long> {
    fun findByRoleType(roleType: RoleType): Role
    fun deleteByRoleType(roleType: RoleType) : List<Role>
}