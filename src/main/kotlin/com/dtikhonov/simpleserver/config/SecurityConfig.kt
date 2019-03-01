package com.dtikhonov.simpleserver.config

import com.dtikhonov.simpleserver.models.RoleType
import com.dtikhonov.simpleserver.services.UserDetailsServiceImpl
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class SecurityConfig(private val customUserDetailsService: UserDetailsServiceImpl,
                     private val passwordEncoderAndMatcher: PasswordEncoder
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
//        http
//            .authorizeRequests()
//            .antMatchers("/").permitAll()
//            .antMatchers("/article/*").authenticated()
//            .antMatchers("/h2-console/**").hasAuthority(RoleType.ADMIN.roleName)
//            .antMatchers("/admin").hasAuthority(RoleType.ADMIN.roleName)
//            .antMatchers("/api").hasAuthority(RoleType.ADMIN.roleName)
//            .and()
//            .formLogin().permitAll()
//            .and()
//            .logout().permitAll()
//            .and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
//            .and().headers().frameOptions().sameOrigin()//allow use of frame to same origin urls
        
        http.authorizeRequests()
            .antMatchers("/h2_console/**").hasAuthority(RoleType.ADMIN.roleName)
            .antMatchers("/api/**").hasAuthority(RoleType.ADMIN.roleName)
            .antMatchers("/api/article/**").hasAuthority(RoleType.ADMIN.roleName)
            .antMatchers("/api/article/").hasAuthority(RoleType.ADMIN.roleName)
            .antMatchers("/article/").authenticated()
            .antMatchers("/admin").hasAuthority(RoleType.ADMIN.roleName)
            .anyRequest().permitAll()
            .and()
            .formLogin().permitAll()
            .and().csrf().ignoringAntMatchers("/h2_console/**")//don't apply CSRF protection to /h2-console
            .and().headers().frameOptions().sameOrigin()//allow use of frame to same origin urls

    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoderAndMatcher)
    }

}