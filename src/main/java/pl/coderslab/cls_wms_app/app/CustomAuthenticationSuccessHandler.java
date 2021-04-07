package pl.coderslab.cls_wms_app.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pl.coderslab.cls_wms_app.repository.UsersRepository;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private UsersService usersService;
    private UsersRepository usersRepository;

    public CustomAuthenticationSuccessHandler(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
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

