package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.EmailTypes;

import java.util.List;


@Repository
public interface EmailTypesRepository extends JpaRepository<EmailTypes, Long> {

    @Query("Select et from EmailTypes et")
    List<EmailTypes> getEmailTypes();
}
