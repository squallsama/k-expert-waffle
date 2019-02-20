package com.dtikhonov.simpleserver.models

import com.dtikhonov.simpleserver.extensions.toSlug
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Article(
    var title: String,
    var headline: String,
    var content: String,
    @ManyToOne var author: User,
    var slug: String = title.toSlug(),
    var addedAt: LocalDateTime = LocalDateTime.now(),
    @Id @GeneratedValue var id: Long? = null)

@Entity
class User(
    var login: String,
    var firstname: String,
    var lastname: String,
    var description: String? = null,
    @Id @GeneratedValue var id: Long? = null)


@Entity
class Place(
    var name: String,
    var position: String,
    var type: String,
    var description: String? = null,
    @Id @GeneratedValue var id: Long? = null)


@Entity
class Event(
    var name: String,
    var position: String,
    var type: String,
    var description: String? = null,
    @Id @GeneratedValue var id: Long? = null)