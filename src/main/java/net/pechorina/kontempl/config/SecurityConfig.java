package net.pechorina.kontempl.config;

import javax.annotation.Resource;

import net.pechorina.kontempl.filters.AuthenticationTokenProcessingFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Resource(name="localAuthProvider")
	private AuthenticationProvider localAuthProvider;
	
	@Resource(name="unauthorizedEntryPoint")
	private AuthenticationEntryPoint unauthorizedEntryPoint;
	
	@Resource(name="customUserDetailsService")
	private UserDetailsService customUserDetailsService; 
	
	@Resource(name="authenticationTokenProcessingFilter")
	private AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter; 
	
	@Autowired
	public void registerGlobalAuthentication(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(localAuthProvider);
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
        	.userDetailsService(customUserDetailsService)
        	.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint)
        	.and()
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	.and()
        	.addFilterBefore(authenticationTokenProcessingFilter, BasicAuthenticationFilter.class)
            .authorizeRequests()
                .antMatchers("/resources/**", "/public/**", "/pv/**").permitAll()
                .antMatchers("/api/user/authenticate").permitAll()
                .antMatchers("/api/users/**").hasRole("admin")
                .antMatchers("/api/sites/**", "/api/pages/**", "/api/images/**", "/api/sitemap/**").hasRole("editor")
                .anyRequest().authenticated()
            ;
   

    }
}
