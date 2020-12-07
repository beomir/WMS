package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("Select distinct cu from Customer cu join fetch cu.company c JOIN fetch Users u on u.company = c.name where cu.active = true and u.username like ?1 order by cu.name")
    List<Customer> getCustomer(String username);

    @Query("Select cu from Customer cu where cu.active = false")
    List<Customer> getDeactivatedCustomer();

    @Query("Select cu from Customer cu")
    List<Customer> getCustomers();



}
