package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Customer;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.CustomerService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CustomerController {

    private CustomerService customerService;
    private CompanyService companyService;

    @Autowired
    public CustomerController(CustomerService customerService, CompanyService companyService) {
        this.customerService = customerService;

        this.companyService = companyService;
    }

    @GetMapping("customer")
    public String list(Model model) {
        List<Customer> customers = customerService.getCustomer();
        model.addAttribute("customers", customers);
        return "customer";
    }

    @GetMapping("customerDeactivatedList")
    public String customerDeactivatedList(Model model) {
        List<Customer> customersList = customerService.getDeactivatedCustomer();
        model.addAttribute("customers", customersList);
        return "customerDeactivatedList";
    }


    @GetMapping("formCustomer")
    public String customerForm(Model model){
        List<Company> companies = companyService.getCompany();
//        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("customer", new Customer());
        model.addAttribute("companies", companies);
        return "formCustomer";
    }

    @PostMapping("formCustomer")
    public String customerAdd(Customer customer) {
        customerService.add(customer);
        return "redirect:/customer";
    }

    @GetMapping("/deleteCustomer/{id}")
    public String removeCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return "redirect:/customer";
    }

    @GetMapping("/activateCustomer/{id}")
    public String activateCustomer(@PathVariable Long id) {
        customerService.activate(id);
        return "redirect:/customerDeactivatedList";
    }


    @GetMapping("/formEditCustomer/{id}")
    public String updateCustomer(@PathVariable Long id, Model model) {
        Customer customer = customerService.findById(id);
        List<Company> companies = companyService.getCompany();
        model.addAttribute(customer);
        model.addAttribute("companies", companies);
//        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formEditCustomer";
    }

    @PostMapping("formEditCustomer")
    public String edit(Customer customer) {
        customerService.add(customer);
//        usersDetailsService.add(usersDetails);
        return "redirect:/customer";
    }


}
