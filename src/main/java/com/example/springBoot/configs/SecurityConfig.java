package com.example.springBoot.configs;


import com.example.springBoot.models.Student;
import com.example.springBoot.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(log -> log.loginPage("/")
                .usernameParameter("loginName")
                .passwordParameter("loginPassword")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/login-success"));

        http.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/home"));

        http.authorizeHttpRequests(auth-> auth
                .requestMatchers("/","/home","user/registration","user/register","student/register","student/registration","admin/registration","admin/register","user/verify").permitAll()
                .requestMatchers("/user/**","/course/**").hasAnyAuthority(User.Role.USER.name(),User.Role.ADMIN.name())
                .requestMatchers("/view/**").hasAnyAuthority(User.Role.USER.name(),User.Role.ADMIN.name(),User.Role.STUDENT.name())
                .requestMatchers("/student/**").hasAnyAuthority(User.Role.STUDENT.name())
                .requestMatchers("/chat/**").hasAnyAuthority(User.Role.USER.name(), User.Role.ADMIN.name())
                .requestMatchers("/admin/**").hasAnyAuthority(User.Role.ADMIN.name())
                .anyRequest().authenticated()
        );
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
           httpSecurityExceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) ->{
               response.sendRedirect("/access-denied");
                   }
                   );
        });
        return http.build();
    }


}
