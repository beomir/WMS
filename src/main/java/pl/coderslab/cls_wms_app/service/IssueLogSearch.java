package pl.coderslab.cls_wms_app.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class IssueLogSearch {
    public String warehouse;
    public String issueLogFileName;
    public String issueLogContent;
    public String createdBy;
    public String createdFrom;
    public String createdTo;

}
