package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.entity.Warehouse;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("Select u from Users u where u.active = true")
    List<Users> getUsers();

//    @Modifying
//    @Query("update Users u set u.email = :email where u.id = :id")
//    void edit(@Param(value = "id") long id, @Param(value = "email") String email);

    boolean existsByUsername(String username);
    Users getByUsername(String username);

    @Query("Select distinct u from Users u where u.id =?1")
    List<Users> getUser(Long id);
}
