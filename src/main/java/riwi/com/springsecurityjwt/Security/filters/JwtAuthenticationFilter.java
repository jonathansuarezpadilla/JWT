package riwi.com.springsecurityjwt.Security.filters;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import riwi.com.springsecurityjwt.Security.JWT.JWTUtils;
import riwi.com.springsecurityjwt.models.UserEntity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.User;


//El usernameAthenticationFilter -> nos ayuda autenticarnos en la aplicacion?
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {



    private JWTUtils jwtUtils;

    public  JwtAuthenticationFilter(JWTUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }


    //intentar autenticarse
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UserEntity userEntity= null;
        String username= "";
        String password= "";

        try{
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
            username= userEntity.getUsername();
            password= userEntity.getPassword();
        }catch(StreamReadException e){
            throw new RuntimeException(e);

        }catch(DatabindException e){
            throw  new RuntimeException(e);
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User user= (User) authResult.getPrincipal();

        // generando el toke de acceso

        String token = jwtUtils.generateAccessToken(user.getUsername());

        response.addHeader("Authorization", token);

        Map<String,Object> httpResponse= new HashMap<>();
        httpResponse.put("token",token);
        httpResponse.put("Message","Autenticacion Correcta");
        httpResponse.put("username",user.getUsername());


        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }

}
