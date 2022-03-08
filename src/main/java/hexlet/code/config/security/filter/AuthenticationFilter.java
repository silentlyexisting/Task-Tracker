package hexlet.code.config.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.config.security.component.TokenGenerator;
import hexlet.code.dto.LoginDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.stream.Collectors;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();


    private final TokenGenerator tokenGenerator;

    public AuthenticationFilter(AuthenticationManager authenticationManagerBean,
                                RequestMatcher loginRequest,
                                TokenGenerator tokenGenerator) {
        super.setAuthenticationManager(authenticationManagerBean);
        super.setRequiresAuthenticationRequestMatcher(loginRequest);
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) throws AuthenticationException {
        final LoginDto loginData = getLoginData(request);
        final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                loginData.getEmail(),
                loginData.getPassword()
        );
        setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private LoginDto getLoginData(final HttpServletRequest request) throws AuthenticationException {
        try {
            final String json = request.getReader()
                    .lines()
                    .collect(Collectors.joining());
            return MAPPER.readValue(json, LoginDto.class);
        } catch (IOException e) {
            throw new BadCredentialsException("Can't extract login data from request");
        }
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
