package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Customer;
import pl.coderslab.cls_wms_app.repository.CompanyRepository;
import pl.coderslab.cls_wms_app.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void add(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> getCustomer() {
        return customerRepository.getCustomer();
    }

    @Override
    public List<Customer> getDeactivatedCustomer() {
        return customerRepository.getDeactivatedCustomer();
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.getOne(id);
    }

    @Override
    public Customer get(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {
        Customer customer = customerRepository.getOne(id);
        customer.setActive(false);
//        company.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        customerRepository.save(customer);
    }

    @Override
    public void activate(Long id) {
        Customer customer = customerRepository.getOne(id);
        customer.setActive(true);
//        company.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        customerRepository.save(customer);
    }

    @Override
    public void update(Customer customer) {

    }
}
