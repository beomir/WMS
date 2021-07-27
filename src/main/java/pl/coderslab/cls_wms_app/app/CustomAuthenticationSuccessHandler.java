package pl.coderslab.cls_wms_app.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pl.coderslab.cls_wms_app.repository.UsersRepository;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private UsersRepository usersRepository;

    public CustomAuthenticationSuccessHandler(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_SCANNER")) {

            httpServletResponse.sendRedirect("/scanner/" + usersRepository.getUsersbyUsername(SecurityUtils.username()).getActivateToken());
        } else {
            httpServletResponse.sendRedirect("/warehouse");
        }
    }
}

