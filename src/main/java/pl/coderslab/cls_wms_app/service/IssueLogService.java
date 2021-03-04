package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.IssueLog;

import java.util.List;

public interface IssueLogService {

    void add(IssueLog issueLog);

    List<IssueLog> getIssueLogs();

    IssueLog findById(Long id);

    List<IssueLog> getIssueLogByAllCriteria(String issueLogFileName, String createdFrom, String createdTo, String createdBy, String issueLogContent, String warehouse);

    void save(IssueLogSearch issueLogSearch);

}
