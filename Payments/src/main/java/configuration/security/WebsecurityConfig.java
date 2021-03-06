package configuration.security;

import model.authentication.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebsecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired 
	private AuthenticationService authenticationService; 
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .authorizeRequests()
            .antMatchers("/login", "/resources/**").permitAll()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
           // .defaultSuccessUrl("/payments",true)
            .permitAll()
            .and()
        .logout()
        	.logoutUrl("/logout")
        	.logoutSuccessUrl("/login")      
        	.invalidateHttpSession(true)
        	.and()
        .logout()
            .permitAll()
            .and()
        .csrf().
		 	disable()		 	            
        .sessionManagement()
		    .maximumSessions(1)		    
		    .maxSessionsPreventsLogin(false)
		    .expiredUrl("/login?expired");
    	
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

    	auth.
    		userDetailsService(authenticationService).
    		passwordEncoder(new BCryptPasswordEncoder());
    }
	
}
