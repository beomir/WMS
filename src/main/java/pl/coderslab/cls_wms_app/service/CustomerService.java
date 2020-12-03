package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Customer;

import java.util.List;

public interface CustomerService {

    void add(Customer customer);

    List<Customer> getCustomer();

    Customer findById(Long id);

    Customer get(Long id);

    void delete(Long id);

    void update(Customer article);
}
