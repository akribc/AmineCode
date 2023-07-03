package ma.octo.assignement.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.json.JsonMapper;
import ma.octo.assignement.domain.Utilisateur;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private AuthenticationManager authenticationManager;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
        AuthenticationException {
            Utilisateur appUser=new Utilisateur();
            appUser.setUsername(request.getParameter("username"));
            appUser.setPassword(request.getParameter("password"));
            return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword())
            );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
    Authentication authResult) throws IOException, ServletException {
    User authenticatedUser= (User) authResult.getPrincipal();
    Algorithm algorithm=Algorithm.HMAC256("myHMACPrivateKey");
    String jwtAccessToken= JWT
    .create()
    .withSubject(authenticatedUser.getUsername())
    .withExpiresAt(new Date(System.currentTimeMillis()+2*60*1000))
    .withIssuer(request.getRequestURL().toString())
    .withClaim("roles",authenticatedUser.getAuthorities().stream()
    .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
    .sign(algorithm);
    String jwtRefreshToken= JWT
    .create()
    .withSubject(authenticatedUser.getUsername())
    .withExpiresAt(new Date(System.currentTimeMillis()+10*24*3600*1000))
    .withIssuer(request.getRequestURL().toString())
    .sign(algorithm);
    Map<String,String> accessToken=new HashMap<>();
    accessToken.put("Access_Token",jwtAccessToken);
    accessToken.put("Refresh_Token",jwtRefreshToken);
    response.setContentType("application/json");
    new JsonMapper().writeValue(response.getOutputStream(),accessToken);
    }
    
}
