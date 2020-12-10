package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;

import java.util.List;

public interface CompanyService {

    void add(Company company);

    List<Company> getCompany();

    List<Company> getCompanyWithoutAll();

    List<Company> getDeactivatedCompany();

    Company findById(Long id);

    void delete(Long id);

    void activate(Long id);


    List<Company> getCompanyByUsername(String username);
}
