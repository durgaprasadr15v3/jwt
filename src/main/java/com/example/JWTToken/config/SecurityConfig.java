

package com.example.JWTToken.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.JWTToken.filter.JwtAuthFilter;
import com.example.JWTToken.service.UserInfoService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig { 
  
    @Autowired
    @Lazy
    private JwtAuthFilter authFilter; 
  
    // User Creation
    
    /*UserDetailsService is the interface that holds user schema like how my user details should look like.
   // There might be the scenario user have a username, password, OTP or we can say that multi-factor 
    //authentication. So, all this schema will be identified under the User and UserDetailsService.
     
     */


    @Bean
    public UserDetailsService userDetailsService() { 
        return new UserInfoService(); 
    } 
  
    // Configuring HttpSecurity 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
        return http.csrf().disable() 
                .authorizeHttpRequests() 
                .requestMatchers("/auth/welcome", "/auth/addNewUser", "/auth/generateToken").permitAll() 
                .and() 
                .authorizeHttpRequests().requestMatchers("/auth/user/**").authenticated() 
                .and() 
                .authorizeHttpRequests().requestMatchers("/auth/admin/**").authenticated() 
                .and() 
                .sessionManagement() 
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                .and() 
                .authenticationProvider(authenticationProvider()) 
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) 
                .build(); 
    } 
  
    // Password Encoding 
    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    } 
  
    
    //It is very important to know that the Authentication provider is an interface and this 
    //interface uses two other interfaces that are: UserDetailsService and PasswordEncoder.


    @Bean
    public AuthenticationProvider authenticationProvider() { 
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
        authenticationProvider.setUserDetailsService(userDetailsService()); 
        authenticationProvider.setPasswordEncoder(passwordEncoder()); 
        return authenticationProvider; 
    } 
  
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
        return config.getAuthenticationManager(); 
    } 
  /*
   * Security Context
Security Context is a place where user can see his/her data once he/she authenticates themself, and 
identify whether he/she is a valid user or not. And this information of the user will automatically 
store inside the Security Context and responds back to the browser.

For the second time, attempts to pass the same security information and same flow will not 
execute further as the user’s credentials are validated already.
*/

}