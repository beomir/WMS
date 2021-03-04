package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.TransactionService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class TransactionFixture {

    private final TransactionService transactionService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;


    private List<Transaction> transactionList = Arrays.asList(
            new Transaction(null, "111","Create Reception", null,TimeUtils.timeNowLong(),"system","Reception","Reception number: 20200001 created",null ),
            new Transaction(null, "211","Create Shipment", null,TimeUtils.timeNowLong(),"system","Shipment", "Shipment number: 20200001 created",null),
            new Transaction(null, "212","Close Shipment", null,TimeUtils.timeNowLong(),"system","Shipment","Shipment number: 20200002 closed",null),
            new Transaction(null, "311","Stock surplus", null,TimeUtils.timeNowLong(),"system","Stock","Surplus: 1 qty, Article: 1234567890123456",null)
    );

    @Autowired
    public TransactionFixture(TransactionService transactionService, CompanyService companyService, WarehouseService warehouseService) {
        this.transactionService = transactionService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();
        List<Warehouse> warehouseList = warehouseService.getWarehouse();

        for (Transaction transaction : transactionList) {
            transactionService.add(transaction);
        }

        Transaction transaction1 = transactionList.get(0);
        Transaction transaction2 = transactionList.get(1);
        Transaction transaction3 = transactionList.get(2);
        Transaction transaction4 = transactionList.get(3);

        transaction1.setCompany(companies.get(0));
        transaction2.setCompany(companies.get(0));
        transaction3.setCompany(companies.get(0));
        transaction4.setCompany(companies.get(1));

        transaction1.setWarehouse(warehouseList.get(0));
        transaction2.setWarehouse(warehouseList.get(0));
        transaction3.setWarehouse(warehouseList.get(0));
        transaction4.setWarehouse(warehouseList.get(0));

        transactionService.add(transaction1);
        transactionService.add(transaction2);
        transactionService.add(transaction3);
        transactionService.add(transaction4);
    }
}
