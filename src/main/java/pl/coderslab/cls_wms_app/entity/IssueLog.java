package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class IssueLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String issueLogContent;
    private String issueLogFileName;
    private String issueLogFilePath;

    private String created;
    private String createdBy;

    private String additionalInformation;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    public IssueLog(Long id, String issueLogContent, String issueLogFileName, String created, String createdBy, String additionalInformation,Warehouse warehouse,String issueLogFilePath) {
        this.id = id;
        this.issueLogContent = issueLogContent;
        this.issueLogFileName = issueLogFileName;
        this.created = created;
        this.createdBy = createdBy;
        this.additionalInformation = additionalInformation;
        this.warehouse = warehouse;
        this.issueLogFilePath = issueLogFilePath;
    }
}
