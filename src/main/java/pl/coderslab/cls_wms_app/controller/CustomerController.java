package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Customer;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.CustomerService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CustomerController {

    private final CustomerService customerService;
    private final CompanyService companyService;

    @Autowired
    public CustomerController(CustomerService customerService, CompanyService companyService) {
        this.customerService = customerService;

        this.companyService = companyService;
    }

    @GetMapping("/shipment/customer")
    public String list(Model model) {
        List<Customer> customers = customerService.getCustomer(SecurityUtils.username());
        model.addAttribute("customers", customers);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "customer";
    }

    @GetMapping("/config/customerDeactivatedList")
    public String customerDeactivatedList(Model model) {
        List<Customer> customersList = customerService.getDeactivatedCustomer();
        model.addAttribute("customers", customersList);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "customerDeactivatedList";
    }


    @GetMapping("/shipment/formCustomer")
    public String customerForm(Model model){
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("customer", new Customer());
        model.addAttribute("companies", companies);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formCustomer";
    }

    @PostMapping("/shipment/formCustomer")
    public String customerAdd(Customer customer) {
        customerService.add(customer);
        return "redirect:/shipment/customer";
    }

    @GetMapping("/shipment/deleteCustomer/{id}")
    public String removeCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return "redirect:/shipment/customer";
    }

    @GetMapping("/config/activateCustomer/{id}")
    public String activateCustomer(@PathVariable Long id) {
        customerService.activate(id);
        return "redirect:/config/customerDeactivatedList";
    }


    @GetMapping("/shipment/formEditCustomer/{id}")
    public String updateCustomer(@PathVariable Long id, Model model) {
        Customer customer = customerService.findById(id);
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute(customer);
        model.addAttribute("companies", companies);
        model.addAttribute("localDateTime", LocalDateTime.now());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formEditCustomer";
    }

    @PostMapping("/shipment/formEditCustomer")
    public String edit(Customer customer) {
        customerService.add(customer);
//        usersDetailsService.add(usersDetails);
        return "redirect:/shipment/customer";
    }


}
