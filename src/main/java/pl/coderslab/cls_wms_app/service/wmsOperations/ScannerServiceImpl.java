package pl.coderslab.cls_wms_app.service.wmsOperations;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScannerServiceImpl implements ScannerService {
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public ScannerServiceImpl(StockRepository stockRepository, LocationRepository locationRepository) {
        this.stockRepository = stockRepository;

        this.locationRepository = locationRepository;
    }

    @Override
    public List<Stock> locationPreview(String locationName, String companyName, String warehouseName) {
        return stockRepository.getStockContentByLocationNameAndCompanyNameAndWarehouseName(locationName, companyName, warehouseName);
    }

    @Override
    public boolean locationsForArticleFound(String locationName, String articleNumber, String inHowManyLocations, HttpSession session,Company company) {
        try {
            Long.parseLong(articleNumber);
        } catch (IllegalArgumentException e) {
            session.setAttribute("locationsForArticleFound", "Entered article number: " + articleNumber + "is not a number");
            return false;
        }
        try {
            Integer.parseInt(inHowManyLocations);
        } catch (IllegalArgumentException e) {
            session.setAttribute("locationsForArticleFound", "Entered number of locations: " + inHowManyLocations + "is not a number");
            return false;
        }
        Long articleNumberParsed = Long.parseLong(articleNumber);
        int numberOfLocations = Integer.parseInt(inHowManyLocations);

        //List of searched Locations Id
        List<Long> locationNumberInSequencePosition = stockRepository.locationNumberInSequencePosition(locationName,numberOfLocations,articleNumberParsed,company.getName());
        log.error(locationNumberInSequencePosition.toString());
        if(locationNumberInSequencePosition.size() > 0){
            return true;
        }
        else{
            session.setAttribute("locationsForArticleFound", "Only one location with stock found for article: " + articleNumber);
            return false;
        }

    }

    @Override
    public List<Stock> findArticleInNearbyLocations(String locationName, String articleNumberInLocations, String inHowManyLocations, HttpSession session, Company company,String warehouseName) {
        try {
            Long.parseLong(articleNumberInLocations);
        } catch (IllegalArgumentException e) {
           log.error("IllegalArgumentException, articleNumberInLocations: " + articleNumberInLocations);
        }
        try {
            Integer.parseInt(inHowManyLocations);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException, inHowManyLocations: " + inHowManyLocations);
        }

        Long articleNumberParsed = Long.parseLong(articleNumberInLocations);
        int numberOfLocations = Integer.parseInt(inHowManyLocations);

        List<Long> listOfSearchedLocationBySequence = stockRepository.locationNumberInSequencePosition(locationName,numberOfLocations,articleNumberParsed,company.getName());
        log.error("listOfSearchedLocationBySequence: " + listOfSearchedLocationBySequence);
        List<Long> listOfSearchedLocationID = new ArrayList<>();
        for (Long placeInSequence : listOfSearchedLocationBySequence) {
            listOfSearchedLocationID.add(locationRepository.locationId(company.getName(),warehouseName,placeInSequence));
        }
        log.error("listOfSearchedLocationID: " + listOfSearchedLocationID);
        List<Stock> stockList = new ArrayList<>();
        for (Long locationId: listOfSearchedLocationID) {
            stockList.add(stockRepository.getStockByLocationIdAndCompanyNameAndWarehouseNameAndArticleNumber(locationId,company.getName(),warehouseName,articleNumberParsed));
        }
        log.error("stockList: " + stockList);
        return stockList;
    }
}
