package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.service.CompanyService;

import java.util.Arrays;
import java.util.List;

@Component
public class CompanyFixture {
    private CompanyService companyService;



    private List<Company> companyList = Arrays.asList(
            new Company(null, "Strojem", "Kharkiv", "Kołhoznaja 13", "33-444", "Ukraine",false),
            new Company(null, "buDUJEm", "Wrocław", "Krzycka 13", "83-444", "Poland",true),
            new Company(null, "BuildMate", "London", "TimesSquere 13", "33-444", "UK",false),
            new Company(null, "test1", "Lisbon", "Salazara 13", "83-444", "Portugal",true),
            new Company(null, "test2", "Milan", "Mussoliniego 13", "33-444", "Italy",true),
            new Company(null, "test3", "Belgrad", "Mladica 13", "83-444", "Serbia",false),
            new Company(null, "all", "all", "all", "all", "all",false)
    );

    @Autowired
    public CompanyFixture(CompanyService companyService) {
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        for (Company company : companyList) {
            companyService.add(company);
        }
    }
}
