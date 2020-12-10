package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Users;

import java.util.List;

public interface UsersService {

    void add(Users users);

//    void edit(Users users);

    List<Users> getUsers();

    List<Users> getDeactivatedUsers();

    Users findById(Long id);

    List<Users> getUser(Long id);

    void delete(Long id);

    void remove(Long id);

    void activate(Long id);

}
