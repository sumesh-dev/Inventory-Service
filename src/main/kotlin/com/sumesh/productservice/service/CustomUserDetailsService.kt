package com.sumesh.productservice.service

import com.sumesh.productservice.dao.ISellerRepository
import com.sumesh.productservice.dao.IUserRepository
import com.sumesh.productservice.model.Seller
import com.sumesh.productservice.model.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var iSellerRepository: ISellerRepository

    @Autowired
    private lateinit var iUserRepository: IUserRepository

    override fun loadUserByUsername(email: String): UserDetails {
        return try {
            val user: Seller = iSellerRepository.findByEmail(email)
                ?: throw UsernameNotFoundException("User not found with username: $email")
            val list = mutableListOf(SimpleGrantedAuthority(user.role))
            User(user.email, user.password,list)
        } catch (error:UsernameNotFoundException){
            val user: UserDao = iUserRepository.findByEmail(email)
                ?: throw UsernameNotFoundException("User not found with username: $email")
            val list = mutableListOf(SimpleGrantedAuthority(user.role))
            User(user.email, user.password,list)
        }

    }

}