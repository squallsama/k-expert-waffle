package com.dtikhonov.simpleserver.services


import com.dtikhonov.simpleserver.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private val userRepository: UserRepository? = null

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val person = userRepository?.findByUsername(username) ?: throw UsernameNotFoundException("User $username not found!")
        val grantedAuthorities = HashSet<GrantedAuthority>()
        for (role in person.roles) {
            grantedAuthorities.add(SimpleGrantedAuthority
                (role.roleType.roleName))
        }

        return User(
            person.username,
            person.password,
            grantedAuthorities
        )
    }
}