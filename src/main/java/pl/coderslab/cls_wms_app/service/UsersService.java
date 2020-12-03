package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.entity.Warehouse;

import java.util.List;

public interface UsersService {

    void add(Users users);

//    void edit(Users users);

    List<Users> getUsers();

    Users findById(Long id);

    List<Users> getUser(Long id);

    void delete(Long id);

    void update(Users users);
}
