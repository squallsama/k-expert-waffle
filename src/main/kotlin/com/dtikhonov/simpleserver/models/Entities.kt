package com.dtikhonov.simpleserver.models

import com.dtikhonov.simpleserver.extensions.toSlug
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonManagedReference
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.ManyToMany
import javax.persistence.GeneratedValue

enum class RoleType(val roleName: String) {
    ANONYM("ANONYM"), USER("USER"), ADMIN("ADMIN")
}


@Entity
class Article(
    @Column(unique=true)
    var title: String,
    var headline: String,
    var content: String,
    @ManyToOne var author: Person,
    var slug: String = title.toSlug(),
    var addedAt: LocalDateTime = LocalDateTime.now(),
    @Id @GeneratedValue var id: Long? = null)

@Entity
@Table(name = "user")
class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var id: Long ? = null,

    @Column(unique=true)
    var username: String,

    @Column(unique=true)
    var email: String,

    @JsonIgnore
    var password: String,
    
    @Transient
    @JsonIgnore
    var passwordConfirm: String,
    
    @ManyToMany
    @JsonIgnore
    var roles: MutableSet<Role>
)


@Entity
@Table(name = "role")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long ? = null,

    @Column(unique=true)
    var roleType: RoleType,
    
    var roleDescription: String ? = null,
    
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    var users: MutableSet<Person>? = null
)