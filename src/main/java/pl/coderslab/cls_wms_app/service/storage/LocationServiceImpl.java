package pl.coderslab.cls_wms_app.service.storage;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.IssueLog;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.repository.CompanyRepository;
import pl.coderslab.cls_wms_app.repository.LocationRepository;
import pl.coderslab.cls_wms_app.repository.StorageZoneRepository;
import pl.coderslab.cls_wms_app.repository.WarehouseRepository;
import pl.coderslab.cls_wms_app.service.wmsSettings.IssueLogService;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;
import pl.coderslab.cls_wms_app.temporaryObjects.AddLocationToStorageZone;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationNameConstruction;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationSearch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.Integer.*;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    public LocationSearch locationSearch;
    public LocationNameConstruction lNC;
    private final IssueLogService issueLogService;
    private final StorageZoneRepository storageZoneRepository;
    private final WarehouseRepository warehouseRepository;
    private final TransactionService transactionService;
    private final CompanyRepository companyRepository;
    public AddLocationToStorageZone addLocationToStorageZone;
    int counter = 0;
    int theSameStorageZone = 0;
    String locationName;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, LocationSearch locationSearch, LocationNameConstruction lNC, IssueLogService issueLogService, StorageZoneRepository storageZoneRepository, WarehouseRepository warehouseRepository, TransactionService transactionService, CompanyRepository companyRepository, AddLocationToStorageZone addLocationToStorageZone) {
        this.locationRepository = locationRepository;
        this.locationSearch = locationSearch;
        this.lNC = lNC;
        this.issueLogService = issueLogService;
        this.storageZoneRepository = storageZoneRepository;
        this.warehouseRepository = warehouseRepository;
        this.transactionService = transactionService;
        this.companyRepository = companyRepository;
        this.addLocationToStorageZone = addLocationToStorageZone;
    }

    @Override
    public void add(Location location) {
        locationRepository.save(location);
    }

    @Override
    public void addLocation(Location location, LocationNameConstruction locationNameConstruction) {

        modificationOnSingleLocation(location, locationNameConstruction);
        location.setActive(true);
        location.setFreeWeight(location.getMaxWeight());
        location.setVolume(location.getDepth() * location.getHeight() * location.getWidth());
        location.setFreeSpace(location.getVolume());
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        if (locationRepository.findLocationByLocationName(locationName) == null) {
            locationRepository.save(location);
            lNC.setMessage("Locations created successfully. Details saved in transaction");
            log.debug("location: " + location + " created");
            Transaction transaction = new Transaction();
            transaction.setHdNumber(0L);
            transaction.setAdditionalInformation("Location: " + location.getLocationName() + ", created in Warehouse: " + location.getWarehouse().getName());
            transaction.setArticle(0L);
            transaction.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            transaction.setCreatedBy(SecurityUtils.usernameForActivations());
            transaction.setCustomer("");
            transaction.setQuality("");
            transaction.setQuantity(0L);
            transaction.setReceptionNumber(0L);
            transaction.setReceptionStatus("");
            transaction.setShipmentNumber(0L);
            transaction.setShipmentStatus("");
            transaction.setTransactionDescription("Single Location Created");
            transaction.setTransactionType("520");
            transaction.setTransactionGroup("Configuration");
            transaction.setCompany(companyRepository.getOneCompanyByUsername(SecurityUtils.usernameForActivations()));
            transaction.setUnit("");
            transaction.setVendor("");
            transaction.setWarehouse(location.getWarehouse());
            transactionService.add(transaction);
        } else {
            lNC.setMessage("ERROR: Location: " + locationName + " already exists, try with another name. Issue log udpated");
            log.error("location: " + locationName + " already exists");
            IssueLog issueLog = new IssueLog();
            issueLog.setWarehouse(location.getWarehouse());
            issueLog.setIssueLogContent("ERROR: Location: " + locationName + " already exists, try with another name");
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setIssueLogFileName("");
            issueLog.setIssueLogContent("");
            issueLog.setIssueLogFilePath("");
            issueLog.setAdditionalInformation("");
        }

    }

    @Override
    public void editLocation(Location location, LocationNameConstruction locationNameConstruction) {

        modificationOnSingleLocation(location, locationNameConstruction);
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        try {
            if (location.getId() == locationRepository.findLocationByLocationName(locationName).getId()) {
                String changes = "Changes after edit: ";
                Location oldLocation = locationRepository.getOne(location.getId());
                lNC.setMessage("Locations edited successfully. Details saved in transaction");
                log.debug("location: " + location + " edited");
                Transaction transaction = new Transaction();
                transaction.setHdNumber(0L);
                //TODO finish transaction for edit location
                if(!oldLocation.getLocationName().equals(location.getLocationName())){
                    changes = changes + " from: " + oldLocation.getLocationName() + ", to: " + location.getLocationName();
                }
                if(!oldLocation.getLocationType().equals(location.getLocationType())){
                    changes = changes + ",location type from: " + oldLocation.getLocationType() + ", to: " + location.getLocationType();
                }
                if(!oldLocation.getStorageZone().getStorageZoneName().equals(location.getStorageZone().getStorageZoneName()) ){
                    changes = changes + ",storage zone from: " + oldLocation.getStorageZone().getStorageZoneName() + ", to: " + location.getStorageZone().getStorageZoneName();
                }
                if(oldLocation.isMultiItem() != location.isMultiItem()){
                    changes = changes + ",multi item from: " + oldLocation.isMultiItem() + ", to: " + location.isMultiItem();
                }
                if(oldLocation.isHdControl() != location.isHdControl()){
                    changes = changes + ",control HD from: " + oldLocation.isHdControl() + ", to: " + location.isHdControl();
                }
                if(oldLocation.isHdControl() != location.isHdControl()){
                    changes = changes + ",warehouse from: " + oldLocation.getWarehouse().getName() + ", to: " + location.getWarehouse().getName();
                }
                if(oldLocation.getMaxWeight() != location.getMaxWeight()){
                    changes = changes + ",max weight from: " + oldLocation.getMaxWeight() + ", to: " + location.getMaxWeight();
                    location.setFreeWeight(location.getMaxWeight() - (oldLocation.getMaxWeight()-oldLocation.getFreeWeight()));
                }
                else{
                    location.setFreeWeight(oldLocation.getFreeWeight());
                }
                if(oldLocation.getWidth() != location.getWidth()){
                    changes = changes + ",width from: " + oldLocation.getWidth() + ", to: " + location.getWidth();
                }
                if(oldLocation.getDepth() != location.getDepth()){
                    changes = changes + ",depth from: " + oldLocation.getDepth() + ", to: " + location.getDepth();
                }
                if(oldLocation.getHeight() != location.getHeight()){
                    changes = changes + ",height from: " + oldLocation.getHeight() + ", to: " + location.getHeight();
                }

                if(oldLocation.isActive() != location.isActive()){
                    changes = changes + ",active from: " + oldLocation.isActive() + ", to: " + location.isActive();
                }
                if(oldLocation.getVolume() != location.getVolume()){
                    location.setVolume(location.getDepth() * location.getHeight() * location.getWidth());
                    changes = changes + ",volume from: " + oldLocation.getVolume() + ", to: " + location.getVolume();
                    location.setFreeSpace(location.getVolume() - (oldLocation.getVolume()-oldLocation.getFreeSpace()));
                }
                else{
                    location.setFreeSpace(oldLocation.getFreeSpace());
                }



                transaction.setAdditionalInformation(changes);
                transaction.setArticle(0L);
                transaction.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                transaction.setCreatedBy(SecurityUtils.usernameForActivations());
                transaction.setCustomer("");
                transaction.setQuality("");
                transaction.setQuantity(0L);
                transaction.setReceptionNumber(0L);
                transaction.setReceptionStatus("");
                transaction.setShipmentNumber(0L);
                transaction.setShipmentStatus("");
                transaction.setTransactionDescription("Single Location Edited");
                transaction.setTransactionType("521");
                transaction.setTransactionGroup("Configuration");
                transaction.setUnit("");
                transaction.setVendor("");
                transaction.setCompany(companyRepository.getOneCompanyByUsername(SecurityUtils.usernameForActivations()));
                transaction.setWarehouse(location.getWarehouse());
                transactionService.add(transaction);
                locationRepository.save(location);
            } else {
                lNC.setMessage("ERROR: Location: " + locationName + " already exists, try with another name. Issue log udpated");
                log.error("location: " + locationName + " already exists");
                IssueLog issueLog = new IssueLog();
                issueLog.setWarehouse(location.getWarehouse());
                issueLog.setIssueLogContent("ERROR: Location: " + locationName + " already exists, try with another name");
                issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                issueLog.setIssueLogFileName("");
                issueLog.setIssueLogContent("");
                issueLog.setIssueLogFilePath("");
                issueLog.setAdditionalInformation("");
            }
        } catch (NullPointerException e) {
            locationRepository.save(location);
        }
    }

    public void modificationOnSingleLocation(Location location, LocationNameConstruction locationNameConstruction) {
        locationDescription(location);
        if (location.getLocationDesc().contains("door")) {
            if (locationNameConstruction.getFirstSepDoor().length() == 4 && StringUtils.isAlpha(locationNameConstruction.getFirstSepDoor()) && locationNameConstruction.getSecondSepDoor().length() == 3 && StringUtils.isAlpha(locationNameConstruction.getSecondSepDoor()) && locationNameConstruction.getThirdSepDoor().length() == 2 && StringUtils.isNumeric(locationNameConstruction.getThirdSepDoor())) {
                locationName = locationNameConstruction.getFirstSepDoor() + locationNameConstruction.getSecondSepDoor() + locationNameConstruction.getThirdSepDoor();
            } else {
                log.error("Location Door name is incorrect: 1sep: " + locationNameConstruction.getFirstSepDoor().length() + StringUtils.isAlpha(locationNameConstruction.getFirstSepDoor()) + " ,2sep: " + locationNameConstruction.getSecondSepDoor().length() + StringUtils.isAlpha(locationNameConstruction.getSecondSepDoor()) + " ,3sep: " + locationNameConstruction.getThirdSepDoor().length() + StringUtils.isAlpha(locationNameConstruction.getThirdSepDoor()));

            }
        } else if (location.getLocationDesc().contains("rack")) {
            if (locationNameConstruction.getFirstSepRack().length() == 3 && StringUtils.isAlpha(locationNameConstruction.getFirstSepRack()) && locationNameConstruction.getSecondSepRack().length() == 3 && StringUtils.isNumeric(locationNameConstruction.getSecondSepRack()) && locationNameConstruction.getThirdSepRack().length() == 2 && StringUtils.isNumeric(locationNameConstruction.getThirdSepRack()) && locationNameConstruction.getFourthSepRack().length() == 3 && StringUtils.isNumeric(locationNameConstruction.getFourthSepRack())) {
                locationName = locationNameConstruction.getFirstSepRack() + locationNameConstruction.getSecondSepRack() + locationNameConstruction.getThirdSepRack() + locationNameConstruction.getFourthSepRack();
            } else {
                log.error("Location Rack name is incorrect: 1sep: " + locationNameConstruction.getFirstSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getFirstSepRack()) + " ,2sep: " + locationNameConstruction.getSecondSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getSecondSepRack()) + " ,3sep: " + locationNameConstruction.getThirdSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getThirdSepRack()) + locationNameConstruction.getFourthSepRack().length() + StringUtils.isAlpha(locationNameConstruction.getFourthSepRack()));
            }
        } else {
            if (locationNameConstruction.getFirstSepFloor().length() == 3 && StringUtils.isAlpha(locationNameConstruction.getFirstSepFloor()) && locationNameConstruction.getSecondSepFloor().length() == 8 && StringUtils.isNumeric(locationNameConstruction.getSecondSepFloor())) {
                locationName = locationNameConstruction.getFirstSepFloor() + locationNameConstruction.getSecondSepFloor();

            } else {
                log.error("Location Floor name is incorrect: " + locationName);
                log.error("Location Door name is incorrect: 1sep: " + locationNameConstruction.getFirstSepFloor().length() + StringUtils.isAlpha(locationNameConstruction.getFirstSepFloor()) + " ,2sep: " + locationNameConstruction.getSecondSepFloor().length() + StringUtils.isAlpha(locationNameConstruction.getSecondSepFloor()));
            }
        }
        location.setLocationName(locationName);
    }

    @Override
    public void addLocationsToStorageZone(AddLocationToStorageZone aLTSZ) {
        String requestedLocationToAdd;
        if (aLTSZ.locationType.equals("RDL") || aLTSZ.locationType.equals("SDL")) {
            log.debug(aLTSZ.storageZone);
            log.debug(aLTSZ.warehouse);
            log.debug(aLTSZ.firstSepDoor);
            log.debug(aLTSZ.secondSepDoor);
            log.debug(aLTSZ.thirdSepDoor);
            log.debug(aLTSZ.thirdSepDoorTo);
            int from = parseInt(aLTSZ.getThirdSepDoor());
            int to = parseInt(aLTSZ.getThirdSepDoorTo());
            int locationsRange = to - from + 1;
            counter = 0;
            theSameStorageZone = 0;
            if (locationsRange > 0) {
                for (int i = from; i <= to; i++) {
                    requestedLocationToAdd = aLTSZ.getFirstSepDoor() + aLTSZ.getSecondSepDoor() + StringUtils.leftPad(Integer.toString(i), 2, "0");
                    log.error("location: " + requestedLocationToAdd + " old storage zone: " + locationRepository.findLocationByLocationName(requestedLocationToAdd).getStorageZone().getStorageZoneName() + ", requested storage zone: " + aLTSZ.storageZone);
                    addLocationsToStorageZoneIssueLogCreation(requestedLocationToAdd, aLTSZ);
                }
                log.error("Counter value: " + counter);
                log.error("doorLocationsRange value: " + locationsRange);
                log.error("theSameStorageZone value: " + theSameStorageZone);
                addLocationsToStorageZoneMessage(locationsRange);
            }
        } else if (aLTSZ.locationType.equals("PFL")) {
            log.debug(aLTSZ.storageZone);
            log.debug(aLTSZ.warehouse);
            log.debug(aLTSZ.firstSepFloor);
            log.debug(aLTSZ.secondSepFloor);
            log.debug(aLTSZ.secondSepFloorTo);
            int from = parseInt(aLTSZ.getSecondSepFloor());
            int to = parseInt(aLTSZ.getSecondSepFloorTo());
            int locationsRange = to - from + 1;
            counter = 0;
            theSameStorageZone = 0;
            if (locationsRange > 0) {
                for (int i = from; i <= to; i++) {
                    requestedLocationToAdd = aLTSZ.getFirstSepFloor() + StringUtils.leftPad(Integer.toString(i), 8, "0");
                    addLocationsToStorageZoneIssueLogCreation(requestedLocationToAdd, aLTSZ);
                }
                log.error("Counter value: " + counter);
                log.error("doorLocationsRange value: " + locationsRange);
                addLocationsToStorageZoneMessage(locationsRange);
            }
        } else {
            log.debug(aLTSZ.storageZone);
            log.debug(aLTSZ.warehouse);
            log.debug(aLTSZ.firstSepFloor);
            log.debug(aLTSZ.secondSepFloor);
            log.debug(aLTSZ.secondSepFloorTo);
            int rackNumberFrom = parseInt(aLTSZ.getSecondSepRack());
            int rackNumberTo = parseInt(aLTSZ.getSecondSepRackTo());
            int rackNumberRange = rackNumberTo - rackNumberFrom + 1;

            int rackHeightFrom = parseInt(aLTSZ.getThirdSepRack());
            int rackHeightTo = parseInt(aLTSZ.getThirdSepRackTo());
            int rackHeightRange = rackHeightTo - rackHeightFrom + 1;

            int locationNumberFrom = parseInt(aLTSZ.getFourthSepRack());
            int locationNumberTo = parseInt(aLTSZ.getFourthSepRackTo());
            int locationNumberRange = locationNumberTo - locationNumberFrom + 1;

            int locationsRange = rackNumberRange * rackHeightRange * locationNumberRange;
            if (rackNumberRange < 0) {
                locationsRange = 0;
            }
            if (rackHeightRange < 0) {
                locationsRange = 0;
            }
            if (locationNumberRange < 1) {
                locationsRange = 0;
            }

            counter = 0;
            theSameStorageZone = 0;
            if (locationsRange > 0) {
                for (int i = rackNumberFrom; i <= rackNumberTo; i++) {
                    String rackNumber;
                    String rackHeight;
                    String locationNbr;
                    rackNumber = StringUtils.leftPad(Integer.toString(i), 3, "0");
                    for (int j = rackHeightFrom; j <= rackHeightTo; j++) {
                        rackHeight = StringUtils.leftPad(Integer.toString(j), 2, "0");
                        for (int k = locationNumberFrom; k <= locationNumberTo; k++) {
                            locationNbr = StringUtils.leftPad(Integer.toString(k), 3, "0");
                            requestedLocationToAdd = aLTSZ.getFirstSepRack() + rackNumber + rackHeight + locationNbr;
                            addLocationsToStorageZoneIssueLogCreation(requestedLocationToAdd, aLTSZ);
                        }
                    }
                }
                log.error("Counter value: " + counter);
                log.error("doorLocationsRange value: " + locationsRange);
                log.error("theSameStorageZone value: " + theSameStorageZone);
                addLocationsToStorageZoneMessage(locationsRange);
            }
        }
    }

    private void addLocationsToStorageZoneIssueLogCreation(String requestedLocationToAdd, AddLocationToStorageZone aLTSZ) {

        if (locationRepository.findLocationByLocationName(requestedLocationToAdd) == null) {
            IssueLog issuelog = new IssueLog();
            issuelog.setIssueLogContent("Location: " + requestedLocationToAdd + ", not exists in DB");
            issuelog.setWarehouse(warehouseRepository.getWarehouseByName(aLTSZ.getWarehouse()));
            issuelog.setCreated(LocalDateTime.now().toString());
            issuelog.setCreatedBy(SecurityUtils.usernameForActivations());
            issuelog.setIssueLogFilePath("");
            issuelog.setIssueLogFileName("");
            issuelog.setAdditionalInformation("Location: " + requestedLocationToAdd + " tried be add to Storage Zone by range generation, but is not exists in DB");
            issueLogService.add(issuelog);
            log.error("Location: " + requestedLocationToAdd + " tried be add to Storage Zone by range generation, but is not exists in DB. Front End validation was broken");

        } else if (locationRepository.findLocationByLocationName(requestedLocationToAdd).getStorageZone().getStorageZoneName().equals(aLTSZ.storageZone)) {
            IssueLog issuelog = new IssueLog();
            issuelog.setIssueLogContent("Location: " + requestedLocationToAdd + ", is already in storage zone: " + aLTSZ.storageZone);
            issuelog.setWarehouse(warehouseRepository.getWarehouseByName(aLTSZ.getWarehouse()));
            issuelog.setCreated(LocalDateTime.now().toString());
            issuelog.setCreatedBy(SecurityUtils.usernameForActivations());
            issuelog.setIssueLogFilePath("");
            issuelog.setIssueLogFileName("");
            issuelog.setAdditionalInformation("Location: " + requestedLocationToAdd + " tried be add to Storage Zone by range generation, but is in the same storage zone as requested");
            issueLogService.add(issuelog);
            theSameStorageZone++;
        } else {
            Location location = locationRepository.findLocationByLocationName(requestedLocationToAdd);
            Transaction transaction = new Transaction();
            transaction.setAdditionalInformation("Location: " + requestedLocationToAdd + " have changed Storage Zone from: " + location.getStorageZone().getStorageZoneName() + ", to: " + aLTSZ.getStorageZone());
            location.setStorageZone(storageZoneRepository.findStorageZoneByStorageZoneName(aLTSZ.getStorageZone()));
            location.setLast_update(LocalDateTime.now().toString());
            transaction.setHdNumber(0L);
            transaction.setArticle(0L);
            transaction.setCustomer("");
            transaction.setVendor("");
            transaction.setReceptionNumber(0L);
            transaction.setReceptionStatus("");
            transaction.setShipmentNumber(0L);
            transaction.setShipmentStatus("");
            transaction.setQuality("");
            transaction.setQuantity(1L);
            transaction.setUnit("");
            transaction.setTransactionGroup("Stock");
            transaction.setCompany(companyRepository.getCompanyByName("all"));
            transaction.setCreatedBy(SecurityUtils.usernameForActivations());
            transaction.setCreated(LocalDateTime.now().toString());
            transaction.setTransactionType("701");
            transaction.setWarehouse(warehouseRepository.getWarehouseByName(aLTSZ.getWarehouse()));
            transaction.setTransactionDescription("Storage Zone for Location Change");
            transactionService.add(transaction);
            add(location);
            log.debug("Storage Zone aLTSSZ: " + aLTSZ.getStorageZone() + " location to save in db value: " + location.getStorageZone().getStorageZoneName());
            counter++;
        }
        log.error("Counter w innej metodzie: " + counter);
        log.error("theSameStorageZone w innej metodzie: " + theSameStorageZone);
    }

    private void addLocationsToStorageZoneMessage(int locationsRange) {
        if (theSameStorageZone > 0) {
            addLocationToStorageZone.setMessage("Requested location was already in requested Storage Zone, check issue log");
            log.error("Requested location was already in requested Storage Zone, check issue log");
        } else if (counter == locationsRange) {
            addLocationToStorageZone.setMessage("All from requested locations have changed Storage Zone");
            log.error("All from requested locations have changed Storage Zone");
        } else {
            addLocationToStorageZone.setMessage("Requested Location is not in DB");
            log.error("Requested Location is not in DB");
        }
    }

    @Override
    public void createLocationPack(Location location, LocationNameConstruction locationNameConstruction) {
        locationDescription(location);
        if (location.getLocationDesc().contains("door")) {
            int from = parseInt(locationNameConstruction.getThirdSepDoor());
            int to = parseInt(locationNameConstruction.getThirdSepDoorTo());
            int locationsRange = to - from;
            if (locationsRange > 0) {
                for (int i = from; i <= to; i++) {
                    Location newLocation = new Location();
                    newLocation.setLocationName(locationNameConstruction.getFirstSepDoor() + locationNameConstruction.getSecondSepDoor() + StringUtils.leftPad(Integer.toString(i), 2, "0"));
                    LocationPackRestData(location, newLocation);
                    newLocation.setMultiItem(true);
                    newLocation.setHdControl(true);
                    createLocationPackIssueLog(location, newLocation);
                }
            } else {
                log.error("ERROR: location range: " + locationsRange + " lower than 1");
            }
        } else if (location.getLocationDesc().contains("floor")) {
            int from = parseInt(locationNameConstruction.getSecondSepFloor());
            int to = parseInt(locationNameConstruction.getSecondSepFloorTo());
            int locationsRange = to - from;
            if (locationsRange > 0) {
                for (int i = from; i <= to; i++) {
                    Location newLocation = new Location();
                    newLocation.setLocationName(locationNameConstruction.getFirstSepFloor() + StringUtils.leftPad(Integer.toString(i), 8, "0"));
                    LocationPackRestData(location, newLocation);
                    log.debug("Location created: " + locationNameConstruction.getFirstSepFloor() + StringUtils.leftPad(Integer.toString(i), 8, "0"));
                    createLocationPackIssueLog(location, newLocation);
                }
            } else {
                log.error("ERROR: location range: " + locationsRange + " lower than 1");
            }
        } else {
            int rackNumberFrom = parseInt(locationNameConstruction.getSecondSepRack());
            int rackNumberTo = parseInt(locationNameConstruction.getSecondSepRackTo());
            int rackNumberRange = rackNumberTo - rackNumberFrom;

            int rackHeightFrom = parseInt(locationNameConstruction.getThirdSepRack());
            int rackHeightTo = parseInt(locationNameConstruction.getThirdSepRackTo());
            int rackHeightRange = rackHeightTo - rackHeightFrom;

            int locationNumberFrom = parseInt(locationNameConstruction.getFourthSepRack());
            int locationNumberTo = parseInt(locationNameConstruction.getFourthSepRackTo());
            int locationNumberRange = locationNumberTo - locationNumberFrom;

            if (rackNumberRange >= 0 && rackHeightRange >= 0 && locationNumberRange >= 0) {
                for (int i = rackNumberFrom; i <= rackNumberTo; i++) {
                    String rackNumber;
                    String rackHeight;
                    String locationNbr;
                    rackNumber = StringUtils.leftPad(Integer.toString(i), 3, "0");
                    for (int j = rackHeightFrom; j <= rackHeightTo; j++) {
                        rackHeight = StringUtils.leftPad(Integer.toString(j), 2, "0");
                        for (int k = locationNumberFrom; k <= locationNumberTo; k++) {
                            locationNbr = StringUtils.leftPad(Integer.toString(k), 3, "0");
                            Location newLocation = new Location();
                            LocationPackRestData(location, newLocation);
                            newLocation.setLocationName(locationNameConstruction.getFirstSepRack() + rackNumber + rackHeight + locationNbr);
                            log.debug("Created location: " + newLocation.getLocationName());
                            createLocationPackIssueLog(location, newLocation);
                        }
                    }
                }
            }
        }
    }

    public void createLocationPackIssueLog(Location location, Location newLocation) {
        if (locationRepository.findLocationByLocationName(newLocation.getLocationName()) == null) {
            locationRepository.save(newLocation);
//            log.debug("Location created: " + locationNameConstruction.getFirstSepDoor() + locationNameConstruction.getSecondSepDoor() + StringUtils.leftPad(Integer.toString(i), 2, "0"));
        } else {
            log.error("Location: " + newLocation.getLocationName() + " already exists and was not created");
            IssueLog issuelog = new IssueLog();
            issuelog.setIssueLogContent("Location: " + newLocation.getLocationName() + ", already exists and was not created");
            lNCMessage(location, issuelog);
        }
    }

    private void lNCMessage(Location location, IssueLog issuelog) {
        lNC.message = "Not all locations from prescribed scope were created, check issue log";
        issuelog.setWarehouse(location.getWarehouse());
        issuelog.setCreated(LocalDateTime.now().toString());
        issuelog.setCreatedBy(SecurityUtils.usernameForActivations());
        issuelog.setIssueLogFilePath("");
        issuelog.setIssueLogFileName("");
        issuelog.setAdditionalInformation("Location tried be create by range generation");
        issueLogService.add(issuelog);
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
        if (location.getLocationType().equals("PFL")) {
            location.setLocationDesc("Picking floor location");
        }
        if (location.getLocationType().equals("PRL")) {
            location.setLocationDesc("Picking rack location");
        }
        if (location.getLocationType().equals("RRL")) {
            location.setLocationDesc("Reserve rack location");
        }
        if (location.getLocationType().equals("RDL")) {
            location.setLocationDesc("Receiving door location");
        }
        if (location.getLocationType().equals("SDL")) {
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
        if (locationName.equals("")) {
            locationName = "%";
        }
        if (storageZoneName.equals("")) {
            storageZoneName = "%";
        }
        return locationRepository.findLocationsByCriteria(locationName, locationType, storageZoneName, warehouseName);
    }

    @Override
    public LocationNameConstruction lCN(Location location) {
        LocationNameConstruction lCN = new LocationNameConstruction();
        if (location.getLocationDesc().contains("door")) {
            lCN.setFirstSepDoor(location.getLocationName().substring(0, 4));
            lCN.setSecondSepDoor(location.getLocationName().substring(4, 7));
            lCN.setThirdSepDoor(location.getLocationName().substring(7, 9));
        } else if (location.getLocationDesc().contains("rack")) {
            lCN.setFirstSepRack(location.getLocationName().substring(0, 3));
            lCN.setSecondSepRack(location.getLocationName().substring(3, 6));
            lCN.setThirdSepRack(location.getLocationName().substring(6, 8));
            lCN.setFourthSepRack(location.getLocationName().substring(8, 11));
        } else {
            lCN.setFirstSepFloor(location.getLocationName().substring(0, 3));
            lCN.setSecondSepFloor(location.getLocationName().substring(3, 11));
        }
        return lCN;
    }
}
