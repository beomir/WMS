package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class TransactionFixture {

    private final TransactionService transactionService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;


    private List<Transaction> transactionList = Arrays.asList(
            new Transaction(null, "111","Reception created manually", null,TimeUtils.timeNowLong(),"system","Reception","Reception number: 20200001 created",null,"Sherpico",null,202000000000000006L,12L,1234567890123455L,"EU1","EA",20200001L,"Created",null,null,null),
            new Transaction(null, "211","Shipment created manually", null,TimeUtils.timeNowLong(),"system","Shipment", "Shipment number: 20200001 created",null,null,"Muzeum 2 wojny",202000000000000005L,10L,1234567890123455L,"EU1","EA",null,null,20200001L,"Created",null),
            new Transaction(null, "212","Shipment Closed", null,TimeUtils.timeNowLong(),"system","Shipment","Shipment number: 20200002 closed",null,null,"Muzeum 2 wojny",202000000000000005L,10L,1234567890123455L,"EU1","EA",null,null,20200001L,"Closed",null),
            new Transaction(null, "311","Stock surplus", null,TimeUtils.timeNowLong(),"system","Stock","Surplus: 1 qty, Article: 1234567890123456",null,null,null,202000000000000005L,10L,1234567890123455L,"EU1","EA",null,null,null,null,null)
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
