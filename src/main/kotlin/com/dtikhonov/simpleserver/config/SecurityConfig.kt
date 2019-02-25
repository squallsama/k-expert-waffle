package com.dtikhonov.simpleserver.config

import com.dtikhonov.simpleserver.services.UserDetailsServiceImpl
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity


@Configuration
@EnableWebSecurity
class SecurityConfig(private val customUserDetailsService: UserDetailsServiceImpl,
                     private val passwordEncoderAndMatcher: PasswordEncoder
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
//        http
//            .authorizeRequests()
//            .antMatchers("/").permitAll()
//            .antMatchers("/article/*").hasAnyRole(RoleType.USER.roleName, RoleType.ADMIN.roleName)
//            .antMatchers("/h2-console/**").hasRole(RoleType.ADMIN.roleName)
//            .antMatchers("/admin").hasRole(RoleType.ADMIN.roleName)
//            .antMatchers("/api").hasRole(RoleType.ADMIN.roleName)
//            .anyRequest().authenticated()
//            .and()
//            .formLogin()
//            .loginPage("/login")
//            .permitAll()
//            .and()
//            .logout()
//                .logoutSuccessUrl("/login")
//            .permitAll()
//            .and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
//            .and().headers().frameOptions().sameOrigin()//allow use of frame to same origin urls
//
//        http.exceptionHandling().accessDeniedPage("/403")
        http.authorizeRequests()
            .antMatchers("/article/**")
            .authenticated()
            .antMatchers("/public/**")
            .permitAll()
            .and()
            .httpBasic()
            .and().csrf().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoderAndMatcher)
    }
}