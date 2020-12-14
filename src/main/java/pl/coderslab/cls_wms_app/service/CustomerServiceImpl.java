package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Customer;
import pl.coderslab.cls_wms_app.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public List<Customer> getCustomer(String username) {
        return customerRepository.getCustomer(username);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.getCustomers();
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
    public void delete(Long id) {
        Customer customer = customerRepository.getOne(id);
        customer.setActive(false);
        customer.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        customer.setChangeBy(SecurityUtils.usernameForActivations());
        customerRepository.save(customer);
    }

    @Override
    public void activate(Long id) {
        Customer customer = customerRepository.getOne(id);
        customer.setActive(true);
        customer.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        customer.setChangeBy(SecurityUtils.usernameForActivations());
        customerRepository.save(customer);
    }

}
