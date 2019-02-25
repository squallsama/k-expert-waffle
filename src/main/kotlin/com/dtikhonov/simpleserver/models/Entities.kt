package com.dtikhonov.simpleserver.models

import com.dtikhonov.simpleserver.extensions.toSlug
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
    var title: String,
    var headline: String,
    var content: String,
    @JsonManagedReference
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

    var username: String,

    var email: String,

    var password: String,
    
    @Transient
    var passwordConfirm: String,
    
    @OneToMany
    @JsonManagedReference
    @JsonIgnoreProperties("roles")
    var roles: MutableSet<Role>
)


@Entity
@Table(name = "role")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long ? = null,
    
    var roleType: RoleType,
    
    var roleDescription: String ? = null,
    
    @ManyToMany(mappedBy = "roles")
    @JsonManagedReference
    @JsonIgnoreProperties("users")
    var users: MutableSet<Person>? = null
)