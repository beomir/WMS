package pl.coderslab.cls_wms_app.app;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static String username() {
//        return SecurityContextHolder.getContext().getAuthentication().getName();

        if(SecurityContextHolder.getContext().getAuthentication().getName().contains("admin"))
        {
            return "%";
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static String usernameForActivations() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}