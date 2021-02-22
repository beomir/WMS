package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.service.CompanyService;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("local")
public class CompanyFixture {
    private CompanyService companyService;



    private List<Company> companyList = Arrays.asList(
            new Company(null, "Strojem", "Kharkiv", "Kołhoznaja 13", "33-444", "Ukraine",false,true, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Company(null, "buDUJEm", "Wrocław", "Krzycka 13", "83-444", "Poland",true,true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Company(null, "BuildMate", "London", "TimesSquere 13", "33-444", "UK",false,true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Company(null, "Fábrica Nacional de Munições de Armas Ligeiras", "Lisbon", "Salazara 13", "83-444", "Portugal",true,true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Company(null, "Eni", "Milan", "Mussoliniego 13", "33-444", "Italy",true,true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Company(null, "Hesteel Serbia", "Belgrad", "Mladica 13", "83-444", "Serbia",false,true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Company(null, "all", "all", "all", "all", "all",false,true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system")
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
