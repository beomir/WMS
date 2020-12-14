package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.repository.CompanyRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService{
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void add(Company company) {
        companyRepository.save(company);
    }

    @Override
    public List<Company> getCompany() {
        return companyRepository.getCompany();
    }

    @Override
    public List<Company> getCompanyWithoutAll() {
        return companyRepository.getCompanyWithoutAll();
    }

    @Override
    public List<Company> getDeactivatedCompany() {
        return companyRepository.getDeactivatedCompany();
    }

    @Override
    public Company findById(Long id) {
        return companyRepository.getOne(id);
    }


    @Override
    public void delete(Long id) {
        Company company = companyRepository.getOne(id);
        company.setActive(false);
        company.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        company.setChangeBy(SecurityUtils.usernameForActivations());
        companyRepository.save(company);
    }

    @Override
    public void activate(Long id) {
        Company company = companyRepository.getOne(id);
        company.setActive(true);
        company.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        company.setChangeBy(SecurityUtils.usernameForActivations());
        companyRepository.save(company);
    }


    @Override
    public List<Company> getCompanyByUsername(String username) {
//        username = SecurityUtils.username();
        return companyRepository.getCompanyByUsername(username);
    }

//    @Override
//    public String getCompanyByUsername(String username) {
//        return companyRepository.getCompanyByUsername(SecurityUtils.username());
//    }
}
