package pl.coderslab.cls_wms_app.service.storage;


import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.StorageZone;
import pl.coderslab.cls_wms_app.temporaryObjects.AddLocationToStorageZone;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationNameConstruction;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationSearch;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface LocationService {



    void add(Location location);

    void addLocation(Location location, LocationNameConstruction locationNameConstruction);

    void editLocation(Location location, LocationNameConstruction locationNameConstruction);

    void createLocationPack(Location location, LocationNameConstruction locationNameConstruction);

    List<Location> getLocations(); //for fixtures

    List<Location> getLocationsByWarehouse(String warehouseName);

    List<Location> getDeactivatedLocations();

    Location findById(Long id);

    void deactivate(Long id);

    void activate(Long id);

    void remove(Long id);

    void save(LocationSearch locationSearching);

    List<Location> getLocationsByAllCriteria(String locationName, String locationType, String storageZoneName, String warehouseName);

    LocationNameConstruction lCN(Location location);

    void addLocationsToStorageZone(AddLocationToStorageZone aLTSZ);

    Location findAvailableLocationAfterProducing(Article article, StorageZone storageZone, String warehouseName);

    Boolean reduceTheAvailableContentOfTheLocation(String locationName,Long articleNumber,Long piecesQty,String warehouseName,String companyName);

    void restoreTheAvailableLocationCapacity(String locationName,Long articleNumber,Long piecesQty,String warehouseName,String companyName);

    String findLocationWithEnoughSpaceAndWeight(Article article, String warehouseName, double articlesWeight, double articlesVolume, Map<String, Double> mapWeight, Map<String, Double> mapVolume,String action);

    void moveBackTemporaryValuesToNormal(Map<String, Double> mapWeight, Map<String, Double> mapVolume,String warehouseName);
}
