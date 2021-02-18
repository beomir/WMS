package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Customer;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.CustomerService;

import java.util.Arrays;
import java.util.List;

@Profile("local")
@Component
@Profile("local")
public class CustomerFixture {
    private CustomerService customerService;
    private CompanyService companyService;

    private List<Customer> customerList = Arrays.asList(
            new Customer(null,null, "Berlin", "Hermana 12", "56-765", "Germany",true,"Muzeum 2 wojny",true, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Customer(null,null, "Stuttgart", "Rudlofa 56", "32-323", "Germany", true,"Mercedes",true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Customer(null,null, "Bonn", "Hermenegildy 12", "77-656", "Germany", true,"Muzeum Stolicy RFN",true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Customer(null,null, "Hamburg", "Brunhildy 19", "99-767", "Germany", true,"Stoczna parowców",true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system")
    );

    @Autowired
    public CustomerFixture(CustomerService customerService, CompanyService companyService) {
        this.customerService = customerService;
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();

        for (Customer customer : customerList) {
            customerService.add(customer);
        }

        Customer customer1 = customerList.get(0);
        Customer customer2 = customerList.get(1);
        Customer customer3 = customerList.get(2);
        Customer customer4 = customerList.get(3);

        customer1.setCompany(companies.get(0));
        customer2.setCompany(companies.get(1));
        customer3.setCompany(companies.get(2));
        customer4.setCompany(companies.get(3));

        customerService.add(customer1);
        customerService.add(customer2);
        customerService.add(customer3);
        customerService.add(customer4);
    }
}
