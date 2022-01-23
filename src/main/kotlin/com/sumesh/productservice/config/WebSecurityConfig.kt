package com.sumesh.productservice.config

import org.json.JSONObject
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.Throws
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.lang.Exception
import java.time.LocalDateTime
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Value("\${allowed.origin}")
    lateinit var origin: String

    @Autowired
    private val jwtRequestFilter: JwtRequestFilter? = null

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {

        httpSecurity
            .csrf().disable()
            .cors().and()
            .authorizeRequests()
            .antMatchers("/product/getProduct/**","/product/getAllProducts","/product/searchByName/**").permitAll()
            .antMatchers("/product/getAllProductsByMe").hasRole("seller")
            .antMatchers("/product/addProduct","/product/delete/**","/product/udate/**","/product/upload").hasAnyRole("seller","admin")
            .antMatchers("/product/getAllProductsBySeller/**").hasRole("admin")
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        httpSecurity.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter::class.java)

        httpSecurity
            .exceptionHandling()
            .accessDeniedHandler{ request, response, e ->
                response.contentType = "application/json;charset=UTF-8"
                response.status = HttpServletResponse.SC_FORBIDDEN
                response.writer.write(
                    JSONObject()
                        .put("timestamp", LocalDateTime.now())
                        .put("message", "Access denied")
                        .toString()
                )
            }
            .authenticationEntryPoint { request, response, e ->
                response.contentType = "application/json;charset=UTF-8"
                response.status = HttpServletResponse.SC_FORBIDDEN
                response.writer.write(
                    JSONObject()
                        .put("timestamp", LocalDateTime.now())
                        .put("message", "Access denied")
                        .toString()
                )
            }

    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = mutableListOf(origin)
        configuration.allowedMethods = mutableListOf("GET", "POST","DELETE","PATCH")
        configuration.allowedHeaders = mutableListOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}