package pl.coderslab.cls_wms_app.temporaryObjects;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class TransactionSearch {
    public String warehouse;
    public String company;
    public String transactionType;
    public String transactionGroup;
    public String transactionUser;
    public String transactionDateFrom;
    public String transactionDateTo;

}
