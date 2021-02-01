package net.opendevup.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity // Pour activer la sécurité web de l'application
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private DataSource dataSource;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("ADMIN","USER");
		auth.inMemoryAuthentication().withUser("user").password("{noop}1234").roles("USER");	
		
		/* * auth.jdbcAuthentication().dataSource(dataSource)
		 * .usersByUsernameQuery("select username as principal, password as credentials, active from users where username =?"
		 * )
		 * .authoritiesByUsernameQuery("select username as principal, roles as role from users_roles where username=?"
		 * ) .rolePrefix("ROLE_").passwordEncoder(new BCryptPasswordEncoder());
		 * */
		 
		/**auth.userDetailsService(userDetailsService)
		    .passwordEncoder(bCryptPasswordEncoder);**/
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login");
		http.authorizeRequests().antMatchers("/Index","/SaveEtudiant","/supprimer","/update","/edit/{id}",
				"/Cours","/formationform","/saveFormation","/supprimerF","/editF/{ID}","/updateF","/download")
					.hasRole("ADMIN");
		http.authorizeRequests().anyRequest().authenticated();
       
		
	}

}
