package hexlet.code.config.filter;

import hexlet.code.config.component.TokenGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenGenerator tokenGenerator;

    public AuthenticationFilter(AuthenticationManager authenticationManagerBean,
                                RequestMatcher loginRequest,
                                TokenGenerator tokenGenerator) {
        super.setAuthenticationManager(authenticationManagerBean);
        super.setRequiresAuthenticationRequestMatcher(loginRequest);
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain,
                                            final Authentication authResult) throws IOException {
        final UserDetails user = (UserDetails) authResult.getPrincipal();
        final String token = tokenGenerator.expiring(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, user.getUsername()));
        response.getWriter().println(token);
    }
}
