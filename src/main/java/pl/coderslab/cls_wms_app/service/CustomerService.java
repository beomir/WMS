package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Customer;

import java.util.List;

public interface CustomerService {

    void add(Customer customer);

    List<Customer> getCustomer(String username);

    List<Customer> getCustomers();

    List<Customer> getDeactivatedCustomer();

    Customer findById(Long id);


    void delete(Long id);

    void activate(Long id);

}
