package pl.coderslab.cls_wms_app.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.IssueLog;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.repository.LocationRepository;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationNameConstruction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.Integer.*;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService{
    private final LocationRepository locationRepository;
    public LocationSearch locationSearch;
    public LocationNameConstruction lNC;
    private final IssueLogService issueLogService;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, LocationSearch locationSearch, LocationNameConstruction lNC, IssueLogService issueLogService) {
        this.locationRepository = locationRepository;
        this.locationSearch = locationSearch;
        this.lNC = lNC;
        this.issueLogService = issueLogService;
    }

    @Override
    public void add(Location location) {
        locationRepository.save(location);
    }

    @Override
    public void addLocation(Location location, LocationNameConstruction locationNameConstruction) {
        locationDescription(location);
        String locationName = "";
        if(location.getLocationDesc().contains("door")){
            if(locationNameConstruction.getFirstSepDoor().length() == 4 && StringUtils.isAlpha(locationNameConstruction.getFirstSepDoor()) && locationNameConstruction.getSecondSepDoor().length() == 3 && StringUtils.isAlpha(locationNameConstruction.getSecondSepDoor()) && locationNameConstruction.getThirdSepDoor().length() == 2 && StringUtils.isNumeric(locationNameConstruction.getThirdSepDoor()) ){
                locationName = locationNameConstruction.getFirstSepDoor() + locationNameConstruction.getSecondSepDoor() + locationNameConstruction.getThirdSepDoor();
                location.setHdControl(true);
                location.setMultiItem(true);
            }
            else{
                log.error("Location Door name is incorrect: 1sep: " + locationNameConstruction.getFirstSepDoor().length() + StringUtils.isAlpha(locationNameConstruction.getFirstSepDoor()) + " ,2sep: " + locationNameConstruction.getSecondSepDoor().length() + StringUtils.isAlpha(locationNameConstruction.getSecondSepDoor()) + " ,3sep: " + locationNameConstruction.getThirdSepDoor().length() + StringUtils.isAlpha(locationNameConstruction.getThirdSepDoor()));

            }
        }
        else if(location.getLocationDesc().contains("rack")){
            if(locationNameConstruction.getFirstSepRack().length() == 3 && StringUtils.isAlpha(locationNameConstruction.getFirstSepRack()) && locationNameConstruction.getSecondSepRack().length() == 3 && StringUtils.isNumeric(locationNameConstruction.getSecondSepRack()) && locationNameConstruction.getThirdSepRack().length() == 2 && StringUtils.isNumeric(locationNameConstruction.getThirdSepRack()) && locationNameConstruction.getFourthSepRack().length() == 3 && StringUtils.isNumeric(locationNameConstruction.getFourthSepRack()) ) {
                locationName = locationNameConstruction.getFirstSepRack() + locationNameConstruction.getSecondSepRack() + locationNameConstruction.getThirdSepRack() + locationNameConstruction.getFourthSepRack();
            }
            else{
                log.error("Location Rack name is incorrect: 1sep: " + locationNameConstruction.getFirstSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getFirstSepRack()) + " ,2sep: " + locationNameConstruction.getSecondSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getSecondSepRack()) + " ,3sep: " + locationNameConstruction.getThirdSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getThirdSepRack()) +  locationNameConstruction.getFourthSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getFourthSepRack()));
            }
        }
        else{
            if(locationNameConstruction.getFirstSepFloor().length() == 3 && StringUtils.isAlpha(locationNameConstruction.getFirstSepFloor()) && locationNameConstruction.getSecondSepFloor().length() == 8 && StringUtils.isNumeric(locationNameConstruction.getSecondSepFloor()) ) {
                locationName = locationNameConstruction.getFirstSepFloor() + locationNameConstruction.getSecondSepFloor();

            }
            else{
                log.error("Location Floor name is incorrect: " + locationName);
                log.error("Location Door name is incorrect: 1sep: " + locationNameConstruction.getFirstSepFloor().length() + StringUtils.isAlpha(locationNameConstruction.getFirstSepFloor()) + " ,2sep: " + locationNameConstruction.getSecondSepFloor().length() + StringUtils.isAlpha(locationNameConstruction.getSecondSepFloor()));
            }
        }
        location.setLocationName(locationName);
        location.setActive(true);
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        if(locationRepository.findLocationByLocationName(locationName) == null)
        {
            locationRepository.save(location);
            locationNameConstruction.message = "";
            log.debug("location: " + location + " created");
        }
        else{
            lNC.setMessage("ERROR: Location: " + locationName + " already exists, try with another name");
            log.error("location: " + locationName + " already exists");
        }

    }

    @Override
    public void editLocation(Location location, LocationNameConstruction locationNameConstruction) {

        locationDescription(location);
        String locationName = "";
        if(location.getLocationDesc().contains("door")){
            if(locationNameConstruction.getFirstSepDoor().length() == 4 && StringUtils.isAlpha(locationNameConstruction.getFirstSepDoor()) && locationNameConstruction.getSecondSepDoor().length() == 3 && StringUtils.isAlpha(locationNameConstruction.getSecondSepDoor()) && locationNameConstruction.getThirdSepDoor().length() == 2 && StringUtils.isNumeric(locationNameConstruction.getThirdSepDoor()) ){
                locationName = locationNameConstruction.getFirstSepDoor() + locationNameConstruction.getSecondSepDoor() + locationNameConstruction.getThirdSepDoor();
            }
            else{
                log.error("Location Door name is incorrect: 1sep: " + locationNameConstruction.getFirstSepDoor().length() + StringUtils.isAlpha(locationNameConstruction.getFirstSepDoor()) + " ,2sep: " + locationNameConstruction.getSecondSepDoor().length() + StringUtils.isAlpha(locationNameConstruction.getSecondSepDoor()) + " ,3sep: " + locationNameConstruction.getThirdSepDoor().length() + StringUtils.isAlpha(locationNameConstruction.getThirdSepDoor()));

            }
        }
        else if(location.getLocationDesc().contains("rack")){
            if(locationNameConstruction.getFirstSepRack().length() == 3 && StringUtils.isAlpha(locationNameConstruction.getFirstSepRack()) && locationNameConstruction.getSecondSepRack().length() == 3 && StringUtils.isNumeric(locationNameConstruction.getSecondSepRack()) && locationNameConstruction.getThirdSepRack().length() == 2 && StringUtils.isNumeric(locationNameConstruction.getThirdSepRack()) && locationNameConstruction.getFourthSepRack().length() == 3 && StringUtils.isNumeric(locationNameConstruction.getFourthSepRack()) ) {
                locationName = locationNameConstruction.getFirstSepRack() + locationNameConstruction.getSecondSepRack() + locationNameConstruction.getThirdSepRack() + locationNameConstruction.getFourthSepRack();
            }
            else{
                log.error("Location Rack name is incorrect: 1sep: " + locationNameConstruction.getFirstSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getFirstSepRack()) + " ,2sep: " + locationNameConstruction.getSecondSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getSecondSepRack()) + " ,3sep: " + locationNameConstruction.getThirdSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getThirdSepRack()) +  locationNameConstruction.getFourthSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getFourthSepRack()));
            }
        }
        else{
            if(locationNameConstruction.getFirstSepFloor().length() == 3 && StringUtils.isAlpha(locationNameConstruction.getFirstSepFloor()) && locationNameConstruction.getSecondSepFloor().length() == 8 && StringUtils.isNumeric(locationNameConstruction.getSecondSepFloor()) ) {
                locationName = locationNameConstruction.getFirstSepFloor() + locationNameConstruction.getSecondSepFloor();

            }
            else{
                log.error("Location Floor name is incorrect: " + locationName);
                log.error("Location Door name is incorrect: 1sep: " + locationNameConstruction.getFirstSepFloor().length() + StringUtils.isAlpha(locationNameConstruction.getFirstSepFloor()) + " ,2sep: " + locationNameConstruction.getSecondSepFloor().length() + StringUtils.isAlpha(locationNameConstruction.getSecondSepFloor()));
            }
        }
        location.setLocationName(locationName);
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        try {
            if(location.getId() == locationRepository.findLocationByLocationName(locationName).getId())
            {
                locationRepository.save(location);
                locationNameConstruction.message = "";
                log.debug("location: " + location + " created");
            }
            else{
                lNC.setMessage("ERROR: Location: " + locationName + " already exists, try with another name");
                log.error("location: " + locationName + " already exists");
            }
        } catch (NullPointerException e){
            locationRepository.save(location);
        }
    }

    @Override
    public void createLocationPack(Location location, LocationNameConstruction locationNameConstruction) {
        locationDescription(location);
        if(location.getLocationDesc().contains("door")){
            int from = parseInt(locationNameConstruction.getThirdSepDoor());
            int to = parseInt(locationNameConstruction.getThirdSepDoorTo());
            int locationsRange = to - from ;
            if(locationsRange>0){
                for (int i = from; i <= to; i++) {
                    Location doorLocation = new Location();
                    doorLocation.setLocationName(locationNameConstruction.getFirstSepDoor() + locationNameConstruction.getSecondSepDoor() + StringUtils.leftPad(Integer.toString(i),2,"0"));
                    doorLocation.setMultiItem(true);
                    doorLocation.setHdControl(true);
                    LocationPackRestData(location, doorLocation);
                    if(locationRepository.findLocationByLocationName(doorLocation.getLocationName()) == null){
                        locationRepository.save(doorLocation);
                        log.debug("Location created: " + locationNameConstruction.getFirstSepDoor() + locationNameConstruction.getSecondSepDoor() + StringUtils.leftPad(Integer.toString(i),2,"0"));
                    }
                    else{
                        log.error("Location: " + doorLocation.getLocationName() + " already exists and was not created");
                        lNC.message = "Not all locations from prescribed scope were created, check issue log";
                        IssueLog issuelog = new IssueLog();
                        issuelog.setIssueLogContent("Location: " + doorLocation.getLocationName() + ", already exists and was not created");
                        issuelog.setWarehouse(location.getWarehouse());
                        issuelog.setCreated(LocalDateTime.now().toString());
                        issuelog.setCreatedBy(SecurityUtils.usernameForActivations());
                        issuelog.setIssueLogFilePath("");
                        issuelog.setIssueLogFileName("");
                        issuelog.setAdditionalInformation("Location tried be create by range generation");
                        issueLogService.add(issuelog);
                    }
                }
            }
            else {
                log.error("ERROR: location range: " + locationsRange + " lower than 1");
            }
        }
        else if(location.getLocationDesc().contains("floor")) {
            int from = parseInt(locationNameConstruction.getSecondSepFloor());
            int to = parseInt(locationNameConstruction.getSecondSepFloorTo());
            int locationsRange = to - from;
            if (locationsRange > 0) {
                for (int i = from; i <= to; i++) {
                    Location floorLocation = new Location();
                    floorLocation.setLocationName(locationNameConstruction.getFirstSepFloor() + StringUtils.leftPad(Integer.toString(i),8,"0"));
                    LocationPackRestData(location, floorLocation);
                    log.debug("Location created: " + locationNameConstruction.getFirstSepFloor() +  StringUtils.leftPad(Integer.toString(i),8,"0"));
                    if(locationRepository.findLocationByLocationName(floorLocation.getLocationName()) == null){
                        locationRepository.save(floorLocation);
                    }
                    else{
                        log.error("Location: " + floorLocation.getLocationName() + " already exists");
                        lNC.message = "Not all locations from prescribed scope were created, check issue log";
                        IssueLog issuelog = new IssueLog();
                        issuelog.setIssueLogContent("Location: " + floorLocation.getLocationName() + ", already exists and was not created");
                        issuelog.setWarehouse(location.getWarehouse());
                        issuelog.setCreated(LocalDateTime.now().toString());
                        issuelog.setCreatedBy(SecurityUtils.usernameForActivations());
                        issuelog.setIssueLogFilePath("");
                        issuelog.setIssueLogFileName("");
                        issuelog.setAdditionalInformation("Location tried be create by range generation");
                        issueLogService.add(issuelog);
                    }
                }
            }
            else {
                log.error("ERROR: location range: " + locationsRange + " lower than 1");
            }
        }
        else{
            int rackNumberFrom = parseInt(locationNameConstruction.getSecondSepRack());
            int rackNumberTo = parseInt(locationNameConstruction.getSecondSepRackTo());
            int rackNumberRange = rackNumberTo - rackNumberFrom;

            int rackHeightFrom = parseInt(locationNameConstruction.getThirdSepRack());
            int rackHeightTo = parseInt(locationNameConstruction.getThirdSepRackTo());
            int rackHeightRange = rackHeightTo - rackHeightFrom;

            int locationNumberFrom = parseInt(locationNameConstruction.getFourthSepRack());
            int locationNumberTo = parseInt(locationNameConstruction.getFourthSepRackTo());
            int locationNumberRange = locationNumberTo - locationNumberFrom;

            if(rackNumberRange >= 0 && rackHeightRange >= 0 && locationNumberRange >= 0){
                for (int i = rackNumberFrom; i <= rackNumberTo; i++) {
                    String rackNumber;
                    String rackHeight;
                    String locationNbr;
                    rackNumber = StringUtils.leftPad(Integer.toString(i),3,"0");
                    for (int j = rackHeightFrom; j <= rackHeightTo; j++) {
                        rackHeight = StringUtils.leftPad(Integer.toString(j),2,"0") ;
                        for (int k = locationNumberFrom; k <= locationNumberTo; k++) {
                            locationNbr = StringUtils.leftPad(Integer.toString(k),3,"0");
                            Location rackLocation = new Location();
                            LocationPackRestData(location, rackLocation);
                            rackLocation.setLocationName(locationNameConstruction.getFirstSepRack() + rackNumber + rackHeight + locationNbr);
                            log.debug("Created location: " +rackLocation.getLocationName());
                            if(locationRepository.findLocationByLocationName(rackLocation.getLocationName()) == null){
                                locationRepository.save(rackLocation);
                            }
                            else{
                                log.error("Location: " + rackLocation.getLocationName() + " already exists");
                                lNC.message = "Not all locations from prescribed scope were created, check issue log";
                                IssueLog issuelog = new IssueLog();
                                issuelog.setIssueLogContent("Location: " + rackLocation.getLocationName() + ", already exists and was not created");
                                issuelog.setWarehouse(location.getWarehouse());
                                issuelog.setCreated(LocalDateTime.now().toString());
                                issuelog.setCreatedBy(SecurityUtils.usernameForActivations());
                                issuelog.setIssueLogFilePath("");
                                issuelog.setIssueLogFileName("");
                                issuelog.setAdditionalInformation("Location tried be create by range generation");
                                issueLogService.add(issuelog);
                            }
                        }
                    }
                }
            }
        }
    }

    private void LocationPackRestData(Location location, Location doorLocation) {
        doorLocation.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        doorLocation.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        doorLocation.setChangeBy(SecurityUtils.usernameForActivations());
        doorLocation.setLocationDesc(location.getLocationDesc());
        doorLocation.setLocationType(location.getLocationType());
        doorLocation.setWarehouse(location.getWarehouse());
        doorLocation.setStorageZone(location.getStorageZone());
        doorLocation.setMultiItem(location.isMultiItem());
        doorLocation.setHdControl(location.isHdControl());
        doorLocation.setActive(true);
    }

    private void locationDescription(Location location) {
        if(location.getLocationType().equals("PFL")){
            location.setLocationDesc("Picking floor location");
        }
        if(location.getLocationType().equals("PRL")){
            location.setLocationDesc("Picking rack location");
        }
        if(location.getLocationType().equals("RRL")){
            location.setLocationDesc("Reserve rack location");
        }
        if(location.getLocationType().equals("RDL")){
            location.setLocationDesc("Receiving door location");
        }
        if(location.getLocationType().equals("SDL")){
            location.setLocationDesc("Shipping door location");
        }
    }


    @Override
    public List<Location> getLocationByWarehouseName(String warehouseName) {
        return locationRepository.getLocationByWarehouseName(warehouseName);
    }

    //for fixtures
    @Override
    public List<Location> getLocations() {
        return locationRepository.getLocations();
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.getOne(id);
    }


    @Override
    public void deactivate(Long id) {
        Location location = locationRepository.getOne(id);
        location.setActive(false);
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        locationRepository.save(location);
    }

    @Override
    public void activate(Long id) {
        Location location = locationRepository.getOne(id);
        location.setActive(true);
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        locationRepository.save(location);
    }

    @Override
    public void remove(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void save(LocationSearch locationSearching) {
        locationSearch.setLocationName(locationSearching.getLocationName());
        locationSearch.setStorageZoneName(locationSearching.getStorageZoneName());
        locationSearch.setWarehouse(locationSearching.getWarehouse());
        locationSearch.setLocationType(locationSearching.getLocationType());
    }

    @Override
    public List<Location> getDeactivatedLocations() {
        return locationRepository.getDeactivatedLocation();
    }

    @Override
    public List<Location> getLocationsByAllCriteria(String locationName, String locationType, String storageZoneName, String warehouseName) {
        if(locationName.equals("")){
            locationName = "%";
        }
        if(storageZoneName.equals("")){
            storageZoneName = "%";
        }
        return locationRepository.findLocationsByCriteria(locationName,locationType,storageZoneName,warehouseName);
    }

    @Override
    public LocationNameConstruction lCN(Location location) {
        LocationNameConstruction lCN = new LocationNameConstruction();
        if(location.getLocationDesc().contains("door")){
           lCN.setFirstSepDoor(location.getLocationName().substring(0,4));
           lCN.setSecondSepDoor(location.getLocationName().substring(4,7));
           lCN.setThirdSepDoor(location.getLocationName().substring(7,9));
        }
        else if(location.getLocationDesc().contains("rack")){
            lCN.setFirstSepRack(location.getLocationName().substring(0,3));
            lCN.setSecondSepRack(location.getLocationName().substring(3,6));
            lCN.setThirdSepRack(location.getLocationName().substring(6,8));
            lCN.setFourthSepRack(location.getLocationName().substring(8,11));
        }
        else{
            lCN.setFirstSepFloor(location.getLocationName().substring(0,3));
            lCN.setSecondSepFloor(location.getLocationName().substring(3,10));
        }
        return lCN;
    }
}
