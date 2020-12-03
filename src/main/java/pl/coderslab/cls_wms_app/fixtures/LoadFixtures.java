package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoadFixtures {
    private StockFixture stockFixture;
    private CompanyFixture companyFixture;
    private WarehouseFixture warehouseFixture;
    private StatusFixture statusFixture;
    private ArticleFixture articleFixture;
    private UsersFixture usersFixture;
//    private UsersDetailsFixture usersDetailsFixture;
    private VendorFixture vendorFixture;
    private ReceptionFixture receptionFixture;
    private ShipMethodFixture shipMethodFixture;
    private CustomerFixture customerFixture;
    private ShipmentFixture shipmentFixture;

    @Autowired
    public LoadFixtures(StockFixture stockFixture, CompanyFixture companyFixture, WarehouseFixture warehouseFixture, StatusFixture statusFixture, ArticleFixture articleFixture, UsersFixture usersFixture,  VendorFixture vendorFixture, ReceptionFixture receptionFixture, ShipMethodFixture shipMethodFixture, CustomerFixture customerFixture, ShipmentFixture shipmentFixture) {
        this.stockFixture = stockFixture;
        this.companyFixture = companyFixture;
        this.usersFixture = usersFixture;
        this.warehouseFixture = warehouseFixture;
        this.statusFixture = statusFixture;
        this.articleFixture = articleFixture;
//        this.usersDetailsFixture = usersDetailsFixture;
        this.vendorFixture = vendorFixture;
        this.receptionFixture = receptionFixture;
        this.shipMethodFixture = shipMethodFixture;
        this.customerFixture = customerFixture;
        this.shipmentFixture = shipmentFixture;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {

        companyFixture.loadIntoDB();
        usersFixture.loadIntoDB();
//        usersDetailsFixture.loadIntoDB();
        warehouseFixture.loadIntoDB();
        statusFixture.loadIntoDB();
        articleFixture.loadIntoDB();
        vendorFixture.loadIntoDB();
        receptionFixture.loadIntoDB();
        shipMethodFixture.loadIntoDB();
        customerFixture.loadIntoDB();
        shipmentFixture.loadIntoDB();

        stockFixture.loadIntoDB();


    }
}
