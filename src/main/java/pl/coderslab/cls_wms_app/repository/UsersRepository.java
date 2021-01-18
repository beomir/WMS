package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Users;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("Select u from Users u where u.active = true")
    List<Users> getUsers();

    @Query("Select u from Users u where u.active = false")
    List<Users> getDeactivatedUsers();

    boolean existsByUsername(String username);
    Users getByUsername(String username);

    @Query("Select distinct u from Users u where u.id =?1")
    List<Users> getUser(Long id);

    @Query("Select u from Users u where u.username = ?1")
    Users getUsersbyUsername(String username);

    @Query("Select u from Users u where u.activateToken = ?1")
    Users getUserByActivateToken(String activateToken);

    @Query(value="Select activate_token from users where username = ?1",nativeQuery = true)
    String FindUsernameByToken(String username);
}
