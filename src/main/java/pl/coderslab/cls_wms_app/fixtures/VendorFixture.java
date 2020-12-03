package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Vendor;
import pl.coderslab.cls_wms_app.service.ArticleService;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.VendorService;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class VendorFixture {

    private VendorService vendorService;
    private CompanyService companyService;


    private List<Vendor> vendorList = Arrays.asList(
            new Vendor(null, null, "Yokohama","Tokugawa 12","10-555","Japan", false),
            new Vendor(null, null, "Istanbul","Altaturk 33","33-444","Turkey", false),
            new Vendor(null, null, "Ciechocinek","Słowacka 122","77-777","Poland", true),
            new Vendor(null, null, "Bydgoszcz","Kopernika 12","44-343","Poland", true)
    );

    @Autowired
    public VendorFixture(VendorService vendorService, CompanyService companyService) {
        this.vendorService = vendorService;
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();

        for (Vendor vendor : vendorList) {
            vendorService.add(vendor);
        }

        Vendor vendor1 = vendorList.get(0);
        Vendor vendor2 = vendorList.get(1);
        Vendor vendor3 = vendorList.get(2);
        Vendor vendor4 = vendorList.get(3);

        vendor1.setCompany(companies.get(0));
        vendor2.setCompany(companies.get(1));
        vendor3.setCompany(companies.get(2));
        vendor4.setCompany(companies.get(3));
        vendorService.add(vendor1);
        vendorService.add(vendor2);
        vendorService.add(vendor3);
        vendorService.add(vendor4);
    }
}
