package pl.coderslab.cls_wms_app.service.wmsOperations;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.ShipmentInCreation;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.repository.ShipmentInCreationRepository;
import pl.coderslab.cls_wms_app.repository.StatusRepository;
import pl.coderslab.cls_wms_app.repository.StockRepository;
import pl.coderslab.cls_wms_app.service.storage.StockService;

import java.util.List;

@Service
@Slf4j
public class ShipmentInCreationServiceImpl implements ShipmentInCreationService{
    private final ShipmentInCreationRepository shipmentInCreationRepository;
    private final StatusRepository statusRepository;
    private final StockService stockService;
    private final StockRepository stockRepository;
    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentInCreationServiceImpl(ShipmentInCreationRepository shipmentInCreationRepository, StatusRepository statusRepository, StockService stockService, StockRepository stockRepository, ShipmentService shipmentService) {
        this.shipmentInCreationRepository = shipmentInCreationRepository;
        this.statusRepository = statusRepository;
        this.stockService = stockService;
        this.stockRepository = stockRepository;
        this.shipmentService = shipmentService;
    }

    @Override
    public void addShipmentInCreation(ShipmentInCreation shipmentInCreation) {
        List<ShipmentInCreation> shipmentInCreationList = shipmentInCreationRepository.getShipmentInCreationByShipmentNumberAndUserNameAndWarehouseName(shipmentInCreation.getShipmentNumber(),shipmentInCreation.getWarehouse().getName(),SecurityUtils.username());
        if(shipmentInCreationList.size() > 0){
            for (ShipmentInCreation shipmentInCreationEdit: shipmentInCreationList) {
                shipmentInCreationEdit.setShipMethod(shipmentInCreation.getShipMethod());
                shipmentInCreationRepository.save(shipmentInCreationEdit);
            }
        }
        shipmentInCreationRepository.save(shipmentInCreation);
    }

    @Override
    public void editShipmentInCreation(ShipmentInCreation shipmentInCreation) {
        List<ShipmentInCreation> shipmentInCreationList = shipmentInCreationRepository.getShipmentInCreationByShipmentNumberAndUserNameAndWarehouseName(shipmentInCreation.getShipmentNumber(),shipmentInCreation.getWarehouse().getName(),SecurityUtils.username());
        for (ShipmentInCreation shipmentInCreationEdit: shipmentInCreationList) {
            shipmentInCreationEdit.setShipMethod(shipmentInCreation.getShipMethod());
            shipmentInCreationRepository.save(shipmentInCreationEdit);
        }
        shipmentInCreationRepository.save(shipmentInCreation);
    }

    @Override
    public List<ShipmentInCreation> getShipmentsListForLoggedUser(String warehouseName,String userName) {
        return shipmentInCreationRepository.getShipmentsListForLoggedUser(warehouseName,userName);
    }

    @Override
    public List<ShipmentInCreation> getShipmentInCreation() {
        return shipmentInCreationRepository.getShipmentInCreation();
    }

    @Override
    public ShipmentInCreation findById(Long id) {
        return shipmentInCreationRepository.getOne(id);
    }


    @Override
    public int qtyOfOpenedShipmentsInCreation(String warehouseName, String username) {
        return shipmentInCreationRepository.qtyOfOpenedShipmentsInCreation(warehouseName, username);
    }

    @Override
    public Long lastShipment() {
        return shipmentInCreationRepository.lastShipment();
    }

    @Override
    public List<ShipmentInCreation> openedShipments(String warehouseName, String username) {
        return shipmentInCreationRepository.openedShipments(warehouseName, username);
    }

    @Override
    public List<Long> stockDifference(String warehouseName, String username) {
        return shipmentInCreationRepository.stockDifference(warehouseName, username);
    }

    @Override
    public List<Long> stockDifferenceQty(String warehouseName, String username) {
        return shipmentInCreationRepository.stockDifferenceQty(warehouseName, username);
    }

    @Override
    public List<Long> shipmentCreationSummary(String warehouseName, String username) {
        return shipmentInCreationRepository.shipmentCreationSummary(warehouseName, username);
    }


    @Override
    public int getCreatedShipmentById(Long shipmentNbr) {
        return shipmentInCreationRepository.getCreatedShipmentById(shipmentNbr);
    }

    @Override
    public void updateCloseCreationShipmentValue(Long shipmentNbr) {
        if(getCreatedShipmentById(shipmentNbr)>0){
            shipmentInCreationRepository.updateCloseCreationShipmentValue(shipmentNbr);
        }
    }


    @Override
    public void closeCreationShipment(Long shipmentNumber, String chosenWarehouse) {
        List<ShipmentInCreation> shipmentInCreationList = shipmentInCreationRepository.getShipmentInCreationByShipmentNumberAndUserNameAndWarehouseName(shipmentNumber,chosenWarehouse,SecurityUtils.username());
        log.error("shipmentInCreationList: " + shipmentInCreationList);
        if(validateTheCorrectnessOfShipment(chosenWarehouse)) {

            //old process based on SQL
//            Long shipmentNmbr = shipmentInCreationRepository.getOne(id).getShipmentNumber();
//            shipmentInCreationRepository.updateCloseCreationShipmentValue(shipmentNmbr);
//            shipmentInCreationRepository.insertDataToShipmentAfterCloseCreation(warehouseId,SecurityUtils.username());
//            shipmentInCreationRepository.updateStockDataAboutShipmentQty(warehouseId,SecurityUtils.username());
//            Long qtyToSendL = shipmentInCreation.getPieces_qty();
//            shipmentInCreationRepository.insertDataToStockAfterCloseCreationWithCorrectStatus(warehouseId,SecurityUtils.username());
//            shipmentInCreationRepository.deleteQtyAfterCloseCreation(warehouseId,SecurityUtils.username());
//            shipmentInCreationRepository.deleteZerosOnStock(warehouseId,SecurityUtils.username());
            for (ShipmentInCreation shipmentInCreation : shipmentInCreationList) {
                int qtyToSend = shipmentInCreation.getPieces_qty().intValue();
                log.error("qtyToSend: " + qtyToSend);
                while (qtyToSend > 0) {
                    String shipMethod = shipmentInCreation.getShipMethod().getMethod();
                    Stock stockToSend = null;
                    log.error("Ship Method: " + shipmentInCreation.getShipMethod().getMethod());
                    if(shipMethod.equals("FIFO")){
                        stockToSend = stockRepository.getStockById(stockRepository.searchStockToSendFIFO(shipmentInCreation.getArticle().getArticle_number(), shipmentInCreation.getWarehouse().getName(),shipmentInCreation.getCompany().getName()));
                   log.error("Fifo direction");
                    } else if(shipMethod.equals("FEFO")){
                        stockToSend = stockRepository.getStockById(stockRepository.searchStockToSendFEFO(shipmentInCreation.getArticle().getArticle_number(), shipmentInCreation.getWarehouse().getName(),shipmentInCreation.getCompany().getName()));
                        log.error("FEFO direction");
                    } else if(shipMethod.equals("LIFO")){
                        stockToSend = stockRepository.getStockById(stockRepository.searchStockToSendLIFO(shipmentInCreation.getArticle().getArticle_number(), shipmentInCreation.getWarehouse().getName(),shipmentInCreation.getCompany().getName()));
                        log.error("LIFO direction");
                    } else if(shipMethod.equals("Order_location_sequence")){
                        stockToSend = stockRepository.getStockById(stockRepository.searchStockToSendOLS(shipmentInCreation.getArticle().getArticle_number(), shipmentInCreation.getWarehouse().getName(),shipmentInCreation.getCompany().getName()));
                        log.error("Order_location_sequence direction");
                    }
                    int qtyToSendOnStock = stockToSend.getPieces_qty().intValue();

                    log.error("Available to send from stock: " + stockToSend.getPieces_qty() + " stock id:" + stockToSend.getId());
                    log.error("Left to be shipped: " + qtyToSend);

                    if (qtyToSendOnStock > qtyToSend) {

                        newShipment(shipmentInCreation, qtyToSend, stockToSend);
                        System.out.println("qtyToSendOnStock > qtyToSend, stock id: " + stockToSend.getId());
                        stockToSend.setPieces_qty(stockToSend.getPieces_qty() - qtyToSend);
                        stockService.add(stockToSend);
                        if (qtyToSend - qtyToSendOnStock < 0) {
                            qtyToSend = 0;
                        } else {
                            qtyToSend = qtyToSend - qtyToSendOnStock;
                        }

                        log.error("on stock more than in shipment: qtyToSend " + qtyToSend);
                    } else if (qtyToSendOnStock == qtyToSend) {
                        qtyToSend = 0;

                        log.error("the same value for send and on stock");
                        newShipment(shipmentInCreation, qtyToSendOnStock, stockToSend);
                        stockService.remove(stockToSend.getId());
                    } else {
                        System.out.println("qtyToSendOnStock < qtyToSend, stock id: " + stockToSend.getId());
                        newShipment(shipmentInCreation, qtyToSendOnStock, stockToSend);
                        stockService.remove(stockToSend.getId());
                        log.error("less on stock than to send");
                        if (qtyToSend - qtyToSendOnStock < 0) {
                            qtyToSend = 0;
                        } else {
                            qtyToSend = qtyToSend - qtyToSendOnStock;
                        }
                    }
                }
                remove(shipmentInCreation.getId());
            }
        }
    }

    private void newShipment(ShipmentInCreation shipmentInCreation, long qtyToSend, Stock stockToSend) {
        Shipment shipment = new Shipment();
        shipment.setArticle(shipmentInCreation.getArticle());
        shipment.setCompany(shipmentInCreation.getCompany());
        shipment.setUnit(shipmentInCreation.getUnit());
        shipment.setCustomer(shipmentInCreation.getCustomer());
        shipment.setHd_number(stockToSend.getHd_number() + 900000000L);
        shipment.setCreation_closed(true);
        shipment.setFinished(false);
        shipment.setPieces_qty(qtyToSend);
        shipment.setLast_update(shipmentInCreation.getLast_update());
        shipment.setCreated(shipmentInCreation.getLast_update());
        shipment.setShipmentNumber(shipmentInCreation.getShipmentNumber());
        shipment.setQuality(shipmentInCreation.getQuality());
        shipment.setShipMethod(shipmentInCreation.getShipMethod());
        shipment.setLocation(shipmentInCreation.getLocation());
        shipment.setStatus(statusRepository.getStatusById(2L));
        shipment.setWarehouse(shipmentInCreation.getWarehouse());
        shipment.setChangeBy(SecurityUtils.username());
        shipmentService.add(shipment);

        Stock stock = new Stock();
        stock.setReceptionNumber(stockToSend.getReceptionNumber());
        stock.setLocation(stockToSend.getLocation());
        stock.setArticle(shipmentInCreation.getArticle());
        stock.setCompany(shipmentInCreation.getCompany());
        stock.setUnit(shipmentInCreation.getUnit());
        stock.setHd_number(shipment.getHd_number());
        stock.setPieces_qty(qtyToSend);
        stock.setLast_update(shipmentInCreation.getLast_update());
        stock.setCreated(shipmentInCreation.getLast_update());
        stock.setShipmentNumber(shipmentInCreation.getShipmentNumber());
        stock.setQuality(shipmentInCreation.getQuality());
        stock.setStatus(statusRepository.getStatusById(2L));
        stock.setChangeBy(SecurityUtils.username());
        stock.setWarehouse(shipmentInCreation.getWarehouse());
        stock.setComment("transferred to send in shipment: " + shipmentInCreation.getShipmentNumber());
        stockService.add(stock);
    }

    @Override
    public Boolean validateTheCorrectnessOfShipment(String chosenWarehouse) {
        List<Long> checkShipments = shipmentInCreationRepository.stockDifferenceQty(chosenWarehouse, SecurityUtils.username());
        boolean shipmentReadyToCloseCreation = true;
        for (int i = 0; i < checkShipments.size(); i++) {
            log.error(String.valueOf(checkShipments.get(i)));
//            System.out.println(checkShipments.get(i));
            if (checkShipments.get(i) < 0) {
                shipmentReadyToCloseCreation = false;
            }
        }
        log.error(String.valueOf(shipmentReadyToCloseCreation));
//        System.out.println(shipmentReadyToCloseCreation);
        return shipmentReadyToCloseCreation;
    }

    @Override
    public String resultOfShipmentCreationValidation(@SessionAttribute String warehouseName){
        String failedAttempt = "Change red underlined qty to pass the validation";
        String attemptSuccessful = "Validation passed successfully. You can close creation without problem";
        if(validateTheCorrectnessOfShipment(warehouseName)){
            return attemptSuccessful;
        }
        else{
            return failedAttempt;
        }
    }

    @Override
    public void remove(Long id) {
        shipmentInCreationRepository.deleteById(id);
    }

//    @Override
//    public void deleteZerosOnStock(@SessionAttribute Long warehouseId, String username)
//    { shipmentInCreationRepository.deleteZerosOnStock(warehouseId,username);
//    }


}
