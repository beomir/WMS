package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.cls_wms_app.entity.Reception;

import java.util.List;

@Repository
public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    @Query("Select r from Reception r join fetch r.article a join fetch r.warehouse w where w.id =?1")
    List<Reception> getReception(Long id);

    @Query(value = "Select reception_number + 1 From receptions order by 1 DESC Limit 1", nativeQuery = true)
    Long lastReception();

    @Query("Select distinct r From Reception r join fetch r.company c join fetch r.warehouse w JOIN fetch Users u on u.company = c.name where r.creation_closed = false and w.id =?1 and u.username like ?2 order by 1 DESC")
    List<Reception> openedReceptions(Long id, String username);

    @Query("Select distinct r from Reception r join fetch r.company c join fetch r.article a join fetch r.warehouse w JOIN fetch Users u on u.company = c.name where w.id =?1 and u.username like ?2 order by r.receptionNumber")
    List<Reception> getReceptions(Long id, String username);

    @Query(value ="Select count(distinct r.reception_number) From receptions r join  company c on r.company_id = c.id join  warehouse w on r.warehouse_id = w.id JOIN  users u on u.company = c.name where r.creation_closed = false and w.id =?1 and u.username like ?2", nativeQuery = true)
    int qtyOfOpenedReceptions(Long id, String username);

    @Query(value = "Select count(creation_closed) from receptions where reception_number = ?1 and creation_closed is true", nativeQuery = true)
    int getCreatedReceptionById(Long receptionNbr);

    @Modifying
    @Transactional
    @Query(value = "update receptions SET creation_closed = true where reception_number = ?1",nativeQuery = true)
    void updateCloseCreationValue(Long receptionNbr);
}
