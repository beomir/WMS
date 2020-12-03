package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.repository.CompanyRepository;
import pl.coderslab.cls_wms_app.repository.StockRepository;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService{
    private CompanyRepository companyRepository;

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
    public Company findById(Long id) {
        return companyRepository.getOne(id);
    }


    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Company company) {

    }
}
