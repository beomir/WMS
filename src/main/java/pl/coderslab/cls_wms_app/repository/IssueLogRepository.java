package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.IssueLog;

import java.util.List;

@Repository
public interface IssueLogRepository extends JpaRepository<IssueLog, Long> {


    @Query("Select i from IssueLog i where i.issueLogFileName like ?1 and i.created > ?2 and i.created < ?3 and i.createdBy like ?4 and i.issueLogContent like ?5 and i.warehouse.name like ?6")
    List<IssueLog> getIssueLogsByFileNameAndIssueLogContentAndCreatedAndCreatedBy (String issueLogFileName, String createdFrom, String createdTo, String createdBy, String issueLogContent, String warehouse);

    @Query("Select i from IssueLog i")
    List<IssueLog> getIssueLogs();

}
