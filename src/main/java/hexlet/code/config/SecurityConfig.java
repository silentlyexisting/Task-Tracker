package hexlet.code.config;

import hexlet.code.config.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static hexlet.code.controller.UserController.USERS_CONTROLLER_PATH;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final RequestMatcher publicUrls;
    private final RequestMatcher loginRequest;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(@Value("${base-url}") final String baseUrl,
                          final UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.loginRequest = new AntPathRequestMatcher(baseUrl + "/login", POST.toString());
        this.publicUrls = new OrRequestMatcher(
                new AntPathRequestMatcher(baseUrl + USERS_CONTROLLER_PATH, POST.toString()),
                new AntPathRequestMatcher(baseUrl + USERS_CONTROLLER_PATH, GET.toString()),
                new NegatedRequestMatcher(new AntPathRequestMatcher(baseUrl + "/**"))
        );
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers(publicUrls).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManagerBean(), loginRequest))
                .sessionManagement().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();

    }



}
