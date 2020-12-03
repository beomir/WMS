package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Company;

import java.util.List;

public interface CompanyService {

    void add(Company company);

    List<Company> getCompany();

    Company findById(Long id);


    void delete(Long id);

    void update(Company company);
}
