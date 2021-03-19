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
        shipmentInCreationRepository.save(shipmentInCreation);
    }

    @Override
    public List<ShipmentInCreation> getShipmentInCreationById(Long id) {
        return shipmentInCreationRepository.getShipmentInCreationById(id);
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
    public int qtyOfOpenedShipmentsInCreation(Long id, String username) {
        return shipmentInCreationRepository.qtyOfOpenedShipmentsInCreation(id, username);
    }

    @Override
    public Long lastShipment() {
        return shipmentInCreationRepository.lastShipment();
    }

    @Override
    public List<ShipmentInCreation> openedShipments(Long id, String username) {
        return shipmentInCreationRepository.openedShipments(id, username);
    }

    @Override
    public List<Long> stockDifference(Long id, String username) {
        return shipmentInCreationRepository.stockDifference(id, username);
    }

    @Override
    public List<Long> stockDifferenceQty(Long id, String username) {
        return shipmentInCreationRepository.stockDifferenceQty(id, username);
    }

    @Override
    public List<Long> shipmentCreationSummary(Long id, String username) {
        return shipmentInCreationRepository.shipmentCreationSummary(id, username);
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
    public void closeCreationShipment(Long id, @SessionAttribute Long warehouseId) {
        ShipmentInCreation shipmentInCreation = shipmentInCreationRepository.getOne(id);
        if(validateTheCorrectnessOfShipment(warehouseId)) {

            //old process based on SQL
//            Long shipmentNmbr = shipmentInCreationRepository.getOne(id).getShipmentNumber();
//            shipmentInCreationRepository.updateCloseCreationShipmentValue(shipmentNmbr);
//            shipmentInCreationRepository.insertDataToShipmentAfterCloseCreation(warehouseId,SecurityUtils.username());
//            shipmentInCreationRepository.updateStockDataAboutShipmentQty(warehouseId,SecurityUtils.username());
//            Long qtyToSendL = shipmentInCreation.getPieces_qty();
//            shipmentInCreationRepository.insertDataToStockAfterCloseCreationWithCorrectStatus(warehouseId,SecurityUtils.username());
//            shipmentInCreationRepository.deleteQtyAfterCloseCreation(warehouseId,SecurityUtils.username());
//            shipmentInCreationRepository.deleteZerosOnStock(warehouseId,SecurityUtils.username());

            int qtyToSend = shipmentInCreation.getPieces_qty().intValue();
            while(qtyToSend>0){
                Stock stockToSend = stockRepository.getStockById(stockRepository.searchStockToSend(shipmentInCreation.getArticle().getArticle_number(),shipmentInCreation.getWarehouse().getName()));
                int qtyToSendOnStock = stockToSend.getPieces_qty().intValue();

                log.debug("Na stoku dostepne do wyslania: " + stockToSend.getPieces_qty() + " id stocku:" + stockToSend.getId());
                log.debug("Pozostało do wyslania: " + qtyToSend);

                if(qtyToSendOnStock > qtyToSend) {

                    newShipment(shipmentInCreation, qtyToSend, stockToSend);
                    System.out.println("qtyToSendOnStock > qtyToSend, stock id: " + stockToSend.getId());
                    stockToSend.setPieces_qty(stockToSend.getPieces_qty() - qtyToSend);
                    stockService.add(stockToSend);
                    if(qtyToSend - qtyToSendOnStock < 0){
                        qtyToSend = 0;
                    }
                    else{
                        qtyToSend = qtyToSend - qtyToSendOnStock;
                    }

                    log.debug("na stoku wiecej niz do wysylki: qtyToSend " + qtyToSend);
                }
                else if(qtyToSendOnStock == qtyToSend){
                    qtyToSend = 0;

                    log.debug("na stoku tyle co do wysylki");
                    newShipment(shipmentInCreation, qtyToSendOnStock, stockToSend);
                    stockService.remove(stockToSend.getId());
                }
                else{
                    System.out.println("qtyToSendOnStock < qtyToSend, stock id: " + stockToSend.getId());
                    newShipment(shipmentInCreation, qtyToSendOnStock, stockToSend);
                    stockService.remove(stockToSend.getId());
                    log.debug("na stoku mniej niz do wysylki");
                    if(qtyToSend - qtyToSendOnStock < 0){
                        qtyToSend = 0;
                    }
                    else{
                        qtyToSend = qtyToSend - qtyToSendOnStock;
                    }
                }
            }

            remove(id);

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
        shipment.setStatus(statusRepository.getStatusById(2L));
        shipment.setWarehouse(shipmentInCreation.getWarehouse());
        shipment.setChangeBy(SecurityUtils.username());
        shipmentService.add(shipment);

        Stock stock = new Stock();
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
    public Boolean validateTheCorrectnessOfShipment(@SessionAttribute Long warehouseId) {
        List<Long> checkShipments = shipmentInCreationRepository.stockDifferenceQty(warehouseId, SecurityUtils.username());
        boolean shipmentReadyToCloseCreation = true;
        for (int i = 0; i < checkShipments.size(); i++) {
            log.debug(String.valueOf(checkShipments.get(i)));
//            System.out.println(checkShipments.get(i));
            if (checkShipments.get(i) < 0) {
                shipmentReadyToCloseCreation = false;
            }
        }
        log.debug(String.valueOf(shipmentReadyToCloseCreation));
//        System.out.println(shipmentReadyToCloseCreation);
        return shipmentReadyToCloseCreation;
    }

    @Override
    public String resultOfShipmentCreationValidation(@SessionAttribute Long warehouseId){
        String failedAttempt = "Change red underlined qty to pass the validation";
        String attemptSuccessful = "Validation passed succesfully. You can close creation without problem";
        if(validateTheCorrectnessOfShipment(warehouseId)){
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
