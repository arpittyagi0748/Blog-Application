package com.io.mountblue.blogapplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select name,password, true from users where name = ?"
        );
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select username,role from roles where username=?"
        );
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(configurer ->
                        configurer
                               .requestMatchers("/customlogin").permitAll()
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/").permitAll()
                               .requestMatchers("/post/**").permitAll()
//                               .requestMatchers("/**").hasAnyRole("ADMIN","AUTHOR")
//                                .requestMatchers("/filterByAuthorAndTag").permitAll()
//                                .requestMatchers("/updateblog/**").permitAll()
                                .requestMatchers("/deletePost/**").hasAnyRole("ADMIN","AUTHOR")
                                  .requestMatchers("/filterByAuthorAndTag/**").permitAll()

                                .requestMatchers("/sort/**").permitAll()
                                .requestMatchers("/myblog/").permitAll()
                                .requestMatchers(HttpMethod.POST,"/myblog/processPost").permitAll()
                                .requestMatchers(HttpMethod.GET,"/myblog/post/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/myblog/updateblog/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/myblog/deletePost/**").permitAll()

                                .requestMatchers(HttpMethod.PUT,"/myblog/postComment/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/myblog/getComment/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/myblog/deleteComment/**").permitAll()
                               .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/customlogin")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout->logout.permitAll()
                );
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
