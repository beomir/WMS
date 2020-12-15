package pl.coderslab.cls_wms_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.repository.UsersRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUserDetailsService implements UserDetailsService {

    private final UsersRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Searching for user by username '{}'", username);

        if (!repository.existsByUsername(username)) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }
        Users users = repository.getByUsername(username);
        return new User(
                users.getUsername()
                , users.getPassword()
                , getAuthoritites(users));
//                ,Collections.singletonList(new SimpleGrantedAuthority("USER")));


//        users.getUsername()
//                , users.getPassword()
//                , Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
    private static Collection<? extends GrantedAuthority> getAuthoritites(Users users) {
        String[] userRoles = users.getUsersRoles().getUsersList().stream().map((usersRoles) -> users.getUsersRoles().getRole()).toArray(String[]::new);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }
}