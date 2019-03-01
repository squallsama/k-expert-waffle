package com.dtikhonov.simpleserver.services


import com.dtikhonov.simpleserver.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    private val log = LoggerFactory.getLogger(UserDetailsServiceImpl::class.java)

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val person = userRepository.findByUsername(username)
        val grantedAuthorities = HashSet<GrantedAuthority>()
        person.roles.forEach {
            run {
                val roleName = it.roleType.roleName
                log.info("Authority $roleName, was granted for $username")
                grantedAuthorities.add(SimpleGrantedAuthority(roleName))
            }
        }

        return User(
            person.username,
            person.password,
            grantedAuthorities
        )
    }
}