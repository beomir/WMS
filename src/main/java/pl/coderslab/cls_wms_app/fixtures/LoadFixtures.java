package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
//@Profile("local")
public class LoadFixtures {
    private StockFixture stockFixture;
    private CompanyFixture companyFixture;
    private WarehouseFixture warehouseFixture;
    private StatusFixture statusFixture;
    private ArticleFixture articleFixture;
    private UsersFixture usersFixture;
    private VendorFixture vendorFixture;
    private ReceptionFixture receptionFixture;
    private ShipMethodFixture shipMethodFixture;
    private CustomerFixture customerFixture;
    private ShipmentFixture shipmentFixture;
    private UnitFixture unitFixture;
    private UsersRolesFixture usersRolesFixture;
    private EmailTypesFixture emailTypesFixture;
    private EmailRecipientsFixture emailRecipientsFixture;
    private SchedulerFixture schedulerFixture;
    private TransactionFixture transactionFixture;
    private LocationFixture locationFixture;
    private StorageZoneFixture storageZoneFixture;


    @Autowired
    public LoadFixtures(StockFixture stockFixture, CompanyFixture companyFixture, WarehouseFixture warehouseFixture, StatusFixture statusFixture, ArticleFixture articleFixture, UsersFixture usersFixture, VendorFixture vendorFixture, ReceptionFixture receptionFixture, ShipMethodFixture shipMethodFixture, CustomerFixture customerFixture, ShipmentFixture shipmentFixture, UnitFixture unitFixture, UsersRolesFixture usersRolesFixture, EmailTypesFixture emailTypesFixture, EmailRecipientsFixture emailRecipientsFixture, SchedulerFixture schedulerFixture, TransactionFixture transactionFixture, LocationFixture locationFixture, StorageZoneFixture storageZoneFixture) {
        this.stockFixture = stockFixture;
        this.companyFixture = companyFixture;
        this.usersFixture = usersFixture;
        this.warehouseFixture = warehouseFixture;
        this.statusFixture = statusFixture;
        this.articleFixture = articleFixture;
        this.vendorFixture = vendorFixture;
        this.receptionFixture = receptionFixture;
        this.shipMethodFixture = shipMethodFixture;
        this.customerFixture = customerFixture;
        this.shipmentFixture = shipmentFixture;
        this.unitFixture = unitFixture;
        this.usersRolesFixture = usersRolesFixture;
        this.emailTypesFixture = emailTypesFixture;
        this.emailRecipientsFixture = emailRecipientsFixture;
        this.schedulerFixture = schedulerFixture;
        this.transactionFixture = transactionFixture;
        this.locationFixture = locationFixture;
        this.storageZoneFixture = storageZoneFixture;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {

        companyFixture.loadIntoDB();
        unitFixture.loadIntoDB();
        usersRolesFixture.loadIntoDB();
        usersFixture.loadIntoDB();
        warehouseFixture.loadIntoDB();
        statusFixture.loadIntoDB();
        articleFixture.loadIntoDB();
        vendorFixture.loadIntoDB();
        receptionFixture.loadIntoDB();
        shipMethodFixture.loadIntoDB();
        customerFixture.loadIntoDB();
        shipmentFixture.loadIntoDB();
        stockFixture.loadIntoDB();
        emailTypesFixture.loadIntoDB();
        emailRecipientsFixture.loadIntoDB();
        schedulerFixture.loadIntoDB();
        transactionFixture.loadIntoDB();
        storageZoneFixture.loadIntoDB();
        locationFixture.loadIntoDB();
    }
}
