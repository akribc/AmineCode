package ma.octo.assignement.config;


import ma.octo.assignement.domain.Utilisateur;
import ma.octo.assignement.filters.JwtAuthenticationFilter;
import ma.octo.assignement.filters.JwtAuthorizationFilter;
import ma.octo.assignement.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UtilisateurService utilisateurService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);

        auth.userDetailsService((username)->{
        Utilisateur appUser=utilisateurService.loadUserByUsername(username);
        if(appUser==null) {
            throw new UsernameNotFoundException("username Not Found!");
        }
        Collection<GrantedAuthority> authorities= appUser.getAppRoles()
        .stream().map((role)-> new SimpleGrantedAuthority(role.getRoleName()))
        .collect(Collectors.toList());
            return new User(appUser.getUsername(),appUser.getPassword(),authorities);
        });
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.authorizeRequests().anyRequest().permitAll();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.formLogin();
        http.authorizeRequests().antMatchers("/login/**","/refreshToken/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/lister_virements/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/lister_versements/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/lister_comptes/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/lister_virements/**").hasAnyAuthority("USER");

        http.authorizeRequests().antMatchers(HttpMethod.POST,"/executer_virement/**").hasAnyAuthority("USER");
//        http.authorizeRequests().antMatchers(HttpMethod.POST,"/roles/**").hasAnyAuthority("ADMIN");
        http.headers().frameOptions().disable();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}