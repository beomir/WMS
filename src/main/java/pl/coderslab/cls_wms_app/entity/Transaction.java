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
    private String vendor;
    private String customer;
    private Long HdNumber;
    private Long quantity;
    private Long article;
    private String quality;
    private String unit;
    private Long receptionNumber;
    private String receptionStatus;
    private Long shipmentNumber;
    private String shipmentStatus;
    private String comment;

    public Transaction(Long id, String transactionType, String transactionDescription, Company company, String created, String createdBy,String transactionGroup,String additionalInformation,Warehouse warehouse,String vendor,String customer,Long HdNumber, Long quantity,Long article,String quality,String unit,Long receptionNumber,String receptionStatus,Long shipmentNumber,String shipmentStatus, String comment)
    {
        this.id = id;
        this.transactionType = transactionType;
        this.transactionDescription = transactionDescription;
        this.company = company;
        this.created = created;
        this.createdBy = createdBy;
        this.transactionGroup = transactionGroup;
        this.additionalInformation = additionalInformation;
        this.warehouse = warehouse;
        this.vendor = vendor;
        this.customer = customer;
        this.HdNumber = HdNumber;
        this.quantity = quantity;
        this.article = article;
        this.quality = quality;
        this.unit = unit;
        this.receptionNumber = receptionNumber;
        this.receptionStatus = receptionStatus;
        this.shipmentNumber = shipmentNumber;
        this.shipmentStatus = shipmentStatus;
        this.comment = comment;
    }
}
