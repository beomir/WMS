package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionType;
    private String transactionDescription;

    @NotNull
    @ManyToOne
    private Company company;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    private String created;
    private String createdBy;
    private String transactionGroup;
    private String additionalInformation;

    public Transaction(Long id, String transactionType, String transactionDescription, Company company, String created,  String createdBy,String transactionGroup,String additionalInformation,Warehouse warehouse) {
        this.id = id;
        this.transactionType = transactionType;
        this.transactionDescription = transactionDescription;
        this.company = company;
        this.created = created;
        this.createdBy = createdBy;
        this.transactionGroup = transactionGroup;
        this.additionalInformation = additionalInformation;
        this.warehouse = warehouse;
    }
}
