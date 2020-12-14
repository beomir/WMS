package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.entity.UsersRoles;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("Select u from Users u where u.active = true")
    List<Users> getUsers();

    @Query("Select u from Users u where u.active = false")
    List<Users> getDeactivatedUsers();

//    @Modifying
//    @Query("update Users u set u.email = :email where u.id = :id")
//    void edit(@Param(value = "id") long id, @Param(value = "email") String email);

    boolean existsByUsername(String username);
    Users getByUsername(String username);

    @Query("Select distinct u from Users u where u.id =?1")
    List<Users> getUser(Long id);


    //To think about solution for this
//    @Modifying
//    @Transactional
//    @Query("update Users u set u.usersRoles = ?1")
//    List<Users> updateRole(Users users);

}
