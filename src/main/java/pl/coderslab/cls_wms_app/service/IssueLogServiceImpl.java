package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.IssueLog;
import pl.coderslab.cls_wms_app.repository.IssueLogRepository;

import java.util.List;

@Service
public class IssueLogServiceImpl implements IssueLogService{
    private final IssueLogRepository issueLogRepository;
    public IssueLogSearch issueLogSearch;

    @Autowired
    public IssueLogServiceImpl(IssueLogRepository issueLogRepository, IssueLogSearch issueLogSearch) {
        this.issueLogRepository = issueLogRepository;
        this.issueLogSearch = issueLogSearch;
    }



    @Override
    public void add(IssueLog issueLog) {
        issueLogRepository.save(issueLog);
    }

    @Override
    public List<IssueLog> getIssueLogs() {
        return issueLogRepository.getIssueLogs();
    }

    @Override
    public IssueLog findById(Long id) {
        return issueLogRepository.getOne(id);
    }


    @Override
    public List<IssueLog> getIssueLogByAllCriteria(String issueLogFileName, String createdFrom, String createdTo, String createdBy, String issueLogContent, String warehouse) {
        if(createdBy.equals("")){
            createdBy = "%";
        }
        if(issueLogFileName.equals("")){
            issueLogFileName = "%";
        }
        if(createdFrom.equals("")){
            createdFrom = "1970-01-01";
        }
        if(createdTo.equals("")){
            createdTo = "2222-02-02";
        }
        if(issueLogContent.equals("")){
            issueLogContent = "%";
        }
        return issueLogRepository.getIssueLogsByFileNameAndIssueLogContentAndCreatedAndCreatedBy(issueLogFileName,createdFrom,createdTo,createdBy,issueLogContent,warehouse);
    }

    @Override
    public void save(IssueLogSearch issueLogSearching) {
        issueLogSearch.setIssueLogContent(issueLogSearching.getIssueLogContent());
        issueLogSearch.setIssueLogFileName(issueLogSearching.getIssueLogFileName());
        issueLogSearch.setCreatedBy(issueLogSearching.getCreatedBy());
        issueLogSearch.setCreatedFrom(issueLogSearching.getCreatedFrom());
        issueLogSearch.setCreatedTo(issueLogSearching.getCreatedTo());
        issueLogSearch.setWarehouse(issueLogSearching.getWarehouse());

    }

}
