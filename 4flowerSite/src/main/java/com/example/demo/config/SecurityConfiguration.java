package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.service.user.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserService userService;

  @Bean
  public DaoAuthenticationProvider authenticationProvider(UserService userService) {
      DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
      authenticationProvider.setUserDetailsService(userService);
      authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
      return authenticationProvider;
  }
  @Override
  public void configure(WebSecurity web) {
//      web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
  	 web.ignoring().antMatchers("/error","/resources/**", "/static/**", "/css/**", "/fonts/**", "/js/**", "/images/**", "/icon/**", "/sass/**");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
      auth.authenticationProvider(authenticationProvider(userService));
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http
          .authorizeRequests()
          		.antMatchers("/resources/**", "/static/**", "/css/**", "/fonts/**", "/js/**", "/images/**", "/icon/**", "/sass/**").permitAll()
              .antMatchers("/","/logout","/shop","/login","/gallery/**","/registration").permitAll()
              .antMatchers("/home","/gallery/fileinsert").hasAuthority("ADMIN") // ADMIN 권한의 유저만 /home 에 접근가능
          .anyRequest()
              .authenticated()
              .and().csrf().disable()
          .formLogin()
              .loginPage("/login")
              .failureUrl("/login?error=true")
              .defaultSuccessUrl("/home")
              .usernameParameter("loginId")
              .passwordParameter("password")
          .and()
              .logout()
              .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
              //
              .logoutSuccessUrl("/?logout=1")
              .deleteCookies("JSESSIONID","remember-me")	//cookie삭제
              .invalidateHttpSession(true) //session삭제
          .and()
              .exceptionHandling()
              .accessDeniedPage("/access-denied")
		      .and()
		      		.sessionManagement()
		      		.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		      		.maximumSessions(1)
		      		.maxSessionsPreventsLogin(false)
		      		.expiredUrl("/?duplicate=1");
//      				.sessionRegistry(sessionRegistry());

  }

//  @Bean
//  public SessionRegistry sessionRegistry() {
//      return new SessionRegistryImpl();
//  }
//
//  @Bean
//  public static ServletListenerRegistrationBean httpSessionEventPublisher() {
//      return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
//  }


}
