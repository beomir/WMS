package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Stock;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("Select  c from Company c where c.active = true")
    List<Company> getCompany();

    @Query("Select  c from Company c where c.active = true and c.name <> 'all'")
    List<Company> getCompanyWithoutAll();

    @Query("Select  c from Company c where c.active = false")
    List<Company> getDeactivatedCompany();


    @Query("SELECT distinct c FROM Users u JOIN Company c on u.company = c.name WHERE u.username like ?1 order by c.name")
    List<Company> getCompanyByUsername(String username);

    @Query("select distinct c FROM Users u JOIN Company c on u.company = c.name WHERE u.username = ?1 ")
    Company getOneCompanyByUsername(String username);

    @Query("Select c from Company c where c.name = ?1")
    Company getCompanyByName(String companyName);

}
