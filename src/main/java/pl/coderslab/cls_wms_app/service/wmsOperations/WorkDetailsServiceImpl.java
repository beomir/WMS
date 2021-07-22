package pl.coderslab.cls_wms_app.service.wmsOperations;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.storage.LocationService;
import pl.coderslab.cls_wms_app.service.storage.StockService;
import pl.coderslab.cls_wms_app.service.wmsSettings.ExtremelyService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class WorkDetailsServiceImpl implements WorkDetailsService{
    private final WorkDetailsRepository workDetailsRepository;
    private final TransactionRepository transactionRepository;
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final StockService stockService;
    private final StatusRepository statusRepository;
    private final ReceptionRepository receptionRepository;
    private final ReceptionService receptionService;
    private final ArticleRepository articleRepository;
    private final ProductionRepository productionRepository;
    private final LocationService locationService;
    private final ExtremelyService extremelyService;



    @Autowired
    public WorkDetailsServiceImpl(WorkDetailsRepository workDetailsRepository, TransactionRepository transactionRepository, StockRepository stockRepository, LocationRepository locationRepository, StockService stockService, StatusRepository statusRepository, ReceptionRepository receptionRepository, ReceptionService receptionService, ArticleRepository articleRepository, ProductionRepository productionRepository, LocationService locationService, ExtremelyService extremelyService) {
        this.workDetailsRepository = workDetailsRepository;
        this.transactionRepository = transactionRepository;
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
        this.stockService = stockService;
        this.statusRepository = statusRepository;
        this.receptionRepository = receptionRepository;
        this.receptionService = receptionService;
        this.articleRepository = articleRepository;
        this.productionRepository = productionRepository;
        this.locationService = locationService;
        this.extremelyService = extremelyService;
    }

    @Override
    public void add(WorkDetails workDetails) {
        Transaction transaction = new Transaction();
        transaction.setWarehouse(workDetails.getWarehouse());
        transaction.setCompany(workDetails.getCompany());
        transaction.setTransactionGroup("Configuration");
        transaction.setTransactionDescription("Create new Work: " + workDetails.getWorkDescription() + " with value: " + workDetails.getWorkType() + " for company: " + workDetails.getCompany().getName() + " ,from Warehouse: " + workDetails.getWarehouse().getName() + " from: " + workDetails.getFromLocation().getLocationName() + " to: " + workDetails.getToLocation().getLocationName() );
        transaction.setVendor("");
        transaction.setUnit("");
        transaction.setShipmentStatus("");
        transaction.setCreated("");
        transaction.setQuality("");
        transaction.setShipmentNumber(0L);
        transaction.setReceptionStatus("");
        transaction.setReceptionNumber(0L);
        transaction.setQuantity(0L);
        transaction.setCustomer("");
        transactionRepository.save(transaction);
        workDetailsRepository.save(workDetails);
    }


    @Override
    public List<WorkDetails> getWorkDetails() {
        return workDetailsRepository.getAll();
    }


    @Override
    public WorkDetails findById(Long id) {
        return workDetailsRepository.getOne(id);
    }

    @Override
    public void save(WorkDetails workDetails) {
        workDetailsRepository.save(workDetails);
    }

    @Override
    public void delete(Long id) {
        WorkDetails workDetails = workDetailsRepository.getOne(id);
        Transaction transaction = new Transaction();
        transaction.setWarehouse(workDetails.getWarehouse());
        transaction.setCompany(workDetails.getCompany());
        transaction.setTransactionGroup("Configuration");
        transaction.setTransactionDescription("Create new Work: " + workDetails.getWorkDescription() + " with value: " + workDetails.getWorkType() + " for company: " + workDetails.getCompany().getName() + " ,from Warehouse: " + workDetails.getWarehouse().getName() + " from: " + workDetails.getFromLocation().getLocationName() + " to: " + workDetails.getToLocation().getLocationName() );
        transaction.setVendor("");
        transaction.setUnit("");
        transaction.setShipmentStatus("");
        transaction.setCreated("");
        transaction.setQuality("");
        transaction.setShipmentNumber(0L);
        transaction.setReceptionStatus("");
        transaction.setReceptionNumber(0L);
        transaction.setQuantity(0L);
        transaction.setCustomer("");
        transactionRepository.save(transaction);
        workDetailsRepository.deleteById(id);
    }


    @Override
    public void edit(WorkDetails workDetails) {
        Transaction transaction = new Transaction();
        transaction.setWarehouse(workDetails.getWarehouse());
        transaction.setCompany(workDetails.getCompany());
        transaction.setTransactionGroup("Configuration");
        transaction.setTransactionDescription("Edited  Work: " + workDetails.getWorkNumber() + " with value: " + workDetails.getWorkType() + " for company: " + workDetails.getCompany().getName() + " ,from Warehouse: " + workDetails.getWarehouse().getName() + " from: " + workDetails.getFromLocation().getLocationName() + " to: " + workDetails.getToLocation().getLocationName() );
        transaction.setVendor("");
        transaction.setUnit("");
        transaction.setShipmentStatus("");
        transaction.setCreated("");
        transaction.setQuality("");
        transaction.setShipmentNumber(0L);
        transaction.setReceptionStatus("");
        transaction.setReceptionNumber(0L);
        transaction.setQuantity(0L);
        transaction.setCustomer("");
        transactionRepository.save(transaction);
        workDetailsRepository.save(workDetails);
    }

    @Override
    public void pickUpGoods(String fromLocation, String enteredArticle, String enteredHdNumber, String equipment, String warehouse, String company, String workHandle,Long piecesQty,String workType) {
        log.error("fromLocation: " + fromLocation);
        log.error("enteredArticle: " + enteredArticle);
        log.error("enteredHdNumber: " + enteredHdNumber);
        log.error("equipment: " + equipment);
        log.error("warehouse: " + warehouse);
        log.error("company: " + company);
        log.error("workHandle: " + workHandle);
        Location equipmentLocation = locationRepository.findLocationByLocationName(equipment,warehouse);


        Stock stock = stockRepository.getStockByHdNumberArticleNumberLocation(Long.parseLong(enteredHdNumber),Long.parseLong(enteredArticle),fromLocation,warehouse,company,workHandle);
        stock.setLocation(equipmentLocation);
        stockService.add(stock);

        if(locationService.reduceTheAvailableContentOfTheLocation(equipment,Long.parseLong(enteredArticle),piecesQty,warehouse,company,workType)) {
            locationService.restoreTheAvailableLocationCapacity(fromLocation,Long.parseLong(enteredArticle),piecesQty,warehouse,company);
        }
    }

    @Override
    public void workLineFinish(WorkDetails workDetails,String scannerChosenEquipment){
        //TODO add transaction
        log.error("workDetails.getHdNumber(): " + workDetails.getHdNumber());
        log.error("workDetails.getArticle().getArticle_number(): " + workDetails.getArticle().getArticle_number());
        log.error("scannerChosenEquipment: " + scannerChosenEquipment);
        log.error("workDetails.getWarehouse().getName(): " + workDetails.getWarehouse().getName());
        log.error("workDetails.getCompany().getName(): " + workDetails.getCompany().getName());
        log.error("workDetails.getHandle(): " + workDetails.getHandle());
        log.error("stock: " + stockRepository.getStockByHdNumberArticleNumberLocation(workDetails.getHdNumber(),workDetails.getArticle().getArticle_number(),scannerChosenEquipment,workDetails.getWarehouse().getName(),workDetails.getCompany().getName(),workDetails.getHandle()));

        Stock stock = stockRepository.getStockByHdNumberArticleNumberLocation(workDetails.getHdNumber(),workDetails.getArticle().getArticle_number(),scannerChosenEquipment,workDetails.getWarehouse().getName(),workDetails.getCompany().getName(),workDetails.getHandle());
        stock.setLocation(locationRepository.findLocationByLocationName(workDetails.getToLocation().getLocationName(),workDetails.getWarehouse().getName()));
        if(workDetails.getWorkDescription().equals("Putaway after producing")){
            stock.setStatus(statusRepository.getStatusByStatusName("on_hand","Stock"));
        }
        stockService.add(stock);
        workDetails.setStatus("close");
        workDetailsRepository.save(workDetails);
        if(locationService.reduceTheAvailableContentOfTheLocation(workDetails.getToLocation().getLocationName(),workDetails.getArticle().getArticle_number(),workDetails.getPiecesQty(),workDetails.getWarehouse().getName(),workDetails.getCompany().getName(),workDetails.getWorkType())){
            locationService.restoreTheAvailableLocationCapacity(scannerChosenEquipment,workDetails.getArticle().getArticle_number(),workDetails.getPiecesQty(),workDetails.getWarehouse().getName(),workDetails.getCompany().getName());
        }
    }

    @Override
    public void workFinished(WorkDetails workDetails, HttpSession session){
        //TODO add transaction
        if(workDetails.getWorkType().equals("Reception") && workDetails.getWorkDescription().equals("Reception Put Away")){
            receptionPutAwayFinish(workDetails, session);
        }
        if(workDetails.getWorkType().equals("Production") && workDetails.getWorkDescription().equals("Production picking")){
            productionPickingFinish(workDetails, session);
        }
        if(workDetails.getWorkType().equals("Transfer") && workDetails.getWorkDescription().equals("Stock transfer Work")){
            stockTransferFinish(workDetails,session);
        }
    }

    public void stockTransferFinish(WorkDetails workDetails,HttpSession session){
        List<Stock> stock = stockRepository.getStockByWorkHandleAndWorkDescription(workDetails.getHandle(),"Stock transfer Work");
        log.error("stockTransferFinish Stock List: " + stock);
        for (Stock value:stock) {
            value.setStatus(statusRepository.getStatusByStatusName("on_hand","Stock"));
            value.setComment("");
            stockRepository.save(value);
        }
        session.setAttribute("stockTransferScannerLocationToMessage","Transfer work: " + workDetails.getWorkNumber() + " finished. Goods are available in location: " + workDetails.getToLocation().getLocationName());
    }

    public void receptionPutAwayFinish(WorkDetails workDetails, HttpSession session){
        List<Stock> stock = stockRepository.getStockByWorkHandleAndWorkDescription(workDetails.getHandle(),"Reception Put Away");
            for (Stock value:stock) {
            value.setStatus(statusRepository.getStatusByStatusName("on_hand","Stock"));
            stockRepository.save(value);
            }
        log.error("workDetailsRepository.workTypeByWorkNumber(workDetails.getWorkNumber()): " + workDetailsRepository.workTypeByWorkNumber(workDetails.getWorkNumber()));

        List<Reception> reception = receptionRepository.getReceptionByReceptionNumber(Long.parseLong(workDetails.getHandle()));
            for (Reception value: reception) {
                value.setStatus(statusRepository.getStatusByStatusName("closed","Reception"));
                receptionRepository.save(value);
                receptionService.finishReception(Long.parseLong(workDetails.getHandle()),session);
            }

        workDetailsRepository.save(workDetails);
    }

    public void productionPickingFinish(WorkDetails workDetails, HttpSession session){
        List<Stock> stock = stockRepository.getStockByWorkHandleAndWorkDescription(workDetails.getHandle(),"Production picking");
        for(Stock value : stock){
            value.setStatus(statusRepository.getStatusByStatusName("on_production","Production"));
            stockRepository.save(value);
        }
        WorkDetails productionWork = new WorkDetails();
        productionWork.setWorkType("Production");
        productionWork.setWorkNumber(extremelyService.nextWorkNumber());
        productionWork.setWorkDescription("Producing finish product from collected intermediate articles");
        productionWork.setStatus("open");
        productionWork.setWarehouse(workDetails.getWarehouse());
        productionWork.setHandle(workDetails.getWorkNumber().toString());
        productionWork.setToLocation(workDetails.getToLocation());
        productionWork.setFromLocation(workDetails.getToLocation());
        productionWork.setChangeBy(SecurityUtils.usernameForActivations());
        productionWork.setCompany(workDetails.getCompany());
        productionWork.setArticle(articleRepository.findArticleByArticle_number(productionRepository.getFinishProductSmallDataByProductionNumber(Long.parseLong(workDetails.getHandle())).getFinishProductNumber()));
        productionWork.setPiecesQty(productionRepository.getFinishProductSmallDataByProductionNumber(Long.parseLong(workDetails.getHandle())).getFinishProductPieces());
        productionWork.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        productionWork.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        log.error("hdNumber: " + extremelyService.nextPalletNbr());

        productionWork.setHdNumber(extremelyService.nextPalletNbr());
        workDetailsRepository.save(workDetails);
        workDetailsRepository.save(productionWork);
        session.setAttribute("productionScannerMessage","Work: " + workDetails.getWorkNumber() + " finished. Goods are available to produce on location: " + workDetails.getToLocation().getLocationName() + " by producing work: " +  productionWork.getWorkNumber());
        List<Production> productionList = productionRepository.getProductionListByNumber(workDetails.getWorkNumber());
        long sumOfIntermediateArticlePieces = 0L;
        for (Production singleProduction:productionList) {
            singleProduction.setStatus("Close");
            sumOfIntermediateArticlePieces = sumOfIntermediateArticlePieces + singleProduction.getIntermediateArticlePieces();
        }
        Production produceFinalProduct = new Production();
        produceFinalProduct.setStatus("Produce");
        produceFinalProduct.setFinishProductNumber(productionWork.getArticle().getArticle_number());
        produceFinalProduct.setFinishProductPieces(productionWork.getPiecesQty());
        produceFinalProduct.setProductionNumber(workDetails.getWorkNumber());
        produceFinalProduct.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        produceFinalProduct.setChangeBy(SecurityUtils.usernameForActivations());
        produceFinalProduct.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        produceFinalProduct.setWarehouse(workDetails.getWarehouse());
        produceFinalProduct.setCompany(workDetails.getCompany());
        produceFinalProduct.setIntermediateArticlePieces(sumOfIntermediateArticlePieces);
        produceFinalProduct.setIntermediateArticleNumber(1L);
        productionRepository.save(produceFinalProduct);
    }


    @Override
    public List<WorkDetailsRepository.WorkHeaderList> workHeaderList(String workDetailsWarehouse, String workDetailsCompany, String workDetailsArticle, String workDetailsType, String workDetailsHandle, String workDetailsHandleDevice, String workDetailsStatus, String workDetailsLocationFrom, String workDetailsLocationTo, String workDetailsWorkNumber) {
        if(workDetailsWarehouse == null || workDetailsWarehouse.equals("")){
            workDetailsWarehouse = "%";
        }
        if(workDetailsCompany == null || workDetailsCompany.equals("")){
            workDetailsCompany = "%";
        }
        if(workDetailsArticle == null || workDetailsArticle.equals("")){
            workDetailsArticle = "%";
        }
        if(workDetailsType == null || workDetailsType.equals("")){
            workDetailsType = "%";
        }
        if(workDetailsHandle == null || workDetailsHandle.equals("")){
            workDetailsHandle = "%";
        }
        if(workDetailsHandleDevice == null || workDetailsHandleDevice.equals("")){
            workDetailsHandleDevice = "%";
        }
        if(workDetailsLocationFrom == null || workDetailsLocationFrom.equals("")){
            workDetailsLocationFrom = "%";
        }
        if(workDetailsLocationTo == null || workDetailsLocationTo.equals("")){
            workDetailsLocationTo = "%";
        }
        if(workDetailsWorkNumber == null || workDetailsWorkNumber.equals("")){
            workDetailsWorkNumber = "%";
        }
        log.debug("workDetailsStatus: " + workDetailsStatus);
        return workDetailsRepository.workHeaderList(workDetailsWarehouse,workDetailsCompany,workDetailsArticle,workDetailsType,workDetailsHandle,workDetailsHandleDevice,workDetailsStatus,workDetailsLocationFrom,workDetailsLocationTo,workDetailsWorkNumber);

    }

    @Override
    public void createPutAwayWork(Long productionNumberToConfirm, HttpSession session) throws CloneNotSupportedException {
        log.debug("productionNumberToConfirm: " + productionNumberToConfirm);
        List<WorkDetails> workDetailsToConfirm = workDetailsRepository.getWorkDetailsByWorkNumber(productionNumberToConfirm);
        for (WorkDetails singularWorkToConfirm: workDetailsToConfirm) {
            log.error("id singularWorkToConfirm: " + singularWorkToConfirm.getId());
            WorkDetails putAwayAfterProduction = (WorkDetails) singularWorkToConfirm.clone();
            putAwayAfterProduction.setId(null);
            int workNumberCounter = 1;
            Long finalWorkNumber = Long.parseLong(singularWorkToConfirm.getWorkNumber() + StringUtils.leftPad(String.valueOf(workNumberCounter),2, "0"));
            log.debug("finalWorkNumber before loop: " + finalWorkNumber);
            log.debug("checkIfWorksExistsForHandleProduction: " + workDetailsRepository.checkIfWorksExistsForHandleProduction(finalWorkNumber,singularWorkToConfirm.getWarehouse().getName(),singularWorkToConfirm.getWorkDescription()));
            putAwayAfterProduction.setWorkNumber(extremelyService.nextWorkNumber());
            log.debug("findAvailableLocationAfterProducing: " + locationService.findAvailableLocationAfterProducing(putAwayAfterProduction.getArticle(),putAwayAfterProduction.getArticle().getProductionArticle().getStorageZone(),putAwayAfterProduction.getWarehouse().getName()));
          if(locationService.findAvailableLocationAfterProducing(putAwayAfterProduction.getArticle(),putAwayAfterProduction.getArticle().getProductionArticle().getStorageZone(),putAwayAfterProduction.getWarehouse().getName())==null){
              session.setAttribute("produceScannerMessage","Available location to putaway in storage zone: " + putAwayAfterProduction.getArticle().getProductionArticle().getStorageZone().getStorageZoneName() + " not found ");
              session.setAttribute("putawayLocationAfterProducing","notfound");
              log.error("locationTo null");
          }
            else{
              Location locationTo = locationService.findAvailableLocationAfterProducing(putAwayAfterProduction.getArticle(),putAwayAfterProduction.getArticle().getProductionArticle().getStorageZone(),putAwayAfterProduction.getWarehouse().getName());
              putAwayAfterProduction.setToLocation(locationTo);
              putAwayAfterProduction.setStatus("open");
              putAwayAfterProduction.setWorkDescription("Putaway after producing");
              workDetailsRepository.save(putAwayAfterProduction);
              session.setAttribute("produceScannerMessage","Putaway work: " + putAwayAfterProduction.getWorkNumber() + " successfully created");
              session.setAttribute("putawayLocationAfterProducing","found");
              log.debug("locationTo not null");
          }
        }
    }

    @Override
    public void closeWorkDetail(Long workNumber,String warehouseName){
        log.error("closeWorkDetail workNumber: " + workNumber);
        List<WorkDetails> works = workDetailsRepository.getWorkListByWarehouseAndWorkNumber(workNumber,warehouseName,SecurityUtils.username());
        log.error("closeWorkDetail works: " + works);
        for(WorkDetails singularWork : works){
            singularWork.setStatus("close");
            workDetailsRepository.save(singularWork);
            log.error("closeWorkDetail: " + singularWork.getId() + " " + singularWork.getWorkNumber() + ' ' + singularWork.getStatus());
        }
    }

    @Override
    public void changeStatusAfterStartWork(Long workNumber,String warehouseName){
        List<WorkDetails> works = workDetailsRepository.getWorkListByWarehouseAndWorkNumber(workNumber,warehouseName,"open");
        for(WorkDetails singularWork : works){
            singularWork.setStatus(SecurityUtils.username());
            workDetailsRepository.save(singularWork);
            log.error("changeStatusAfterStartWork: " + singularWork.getId() + " " + singularWork.getWorkNumber());
        }
    }

    @Override
    public void createTransferWork(Stock chosenStockPositional, Stock stock, String locationN){
        WorkDetails transferWork = new WorkDetails();
        log.error("extremelyService.nextWorkNumber(): " + extremelyService.nextWorkNumber());
        transferWork.setWorkNumber(extremelyService.nextWorkNumber());
        transferWork.setCompany(chosenStockPositional.getCompany());
        transferWork.setWarehouse(chosenStockPositional.getWarehouse());
        transferWork.setWorkDescription("Stock transfer Work");
        transferWork.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        transferWork.setWorkType("Transfer");
        transferWork.setArticle(stock.getArticle());
        transferWork.setStatus("open");
        transferWork.setHandle(transferWork.getWorkNumber().toString());
        transferWork.setToLocation(locationRepository.findLocationByLocationName(locationN,stock.getWarehouse().getName()));
        transferWork.setFromLocation(chosenStockPositional.getLocation());
        transferWork.setPiecesQty(chosenStockPositional.getPieces_qty());
        transferWork.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        transferWork.setHdNumber(chosenStockPositional.getHd_number());
        transferWork.setChangeBy(SecurityUtils.username());
        workDetailsRepository.save(transferWork);
        chosenStockPositional.setStatus(statusRepository.getStatusByStatusName("transfer","Stock"));
        chosenStockPositional.setChangeBy(SecurityUtils.username());
        chosenStockPositional.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        chosenStockPositional.setHandle(transferWork.getWorkNumber().toString());
        chosenStockPositional.setComment("Transfer work Number: " + transferWork.getWorkNumber());
        stockRepository.save(chosenStockPositional);
    }

    @Override
    public void assigningWorkLogic(HttpSession session,Long workNumber,String scannerChosenWarehouse) {
        log.error("NextURL value: " + session.getAttribute("nextURL").toString());
        if(session.getAttribute("nextURL").toString().contains("1/1")){
            receptionPutawayWorkSearch(session,workNumber,scannerChosenWarehouse);
        }
        else if(session.getAttribute("nextURL").toString().contains("2/1")){
            stockTransferWorkSearch(session,workNumber,scannerChosenWarehouse);
        }
        else if(session.getAttribute("nextURL").toString().contains("5/1")){
            productionPickingWorkSearch(session,workNumber,scannerChosenWarehouse);
        }
        else if(session.getAttribute("nextURL").toString().contains("5/2")){
            productionProduceWorkSearch(session,workNumber,scannerChosenWarehouse);
        }
        else if(session.getAttribute("nextURL").toString().contains("5/3")){
            productionPutawayWorkSearch(session,workNumber,scannerChosenWarehouse);
        }
    }

    @Override
    public boolean stockTransferWorkSearch(HttpSession session, Long stockTransferWorkNumber, String scannerChosenWarehouse) {
        if(workDetailsRepository.checkIfWorksExistsForHandleProduction(stockTransferWorkNumber,scannerChosenWarehouse,"Stock transfer Work")>0){
            session.setAttribute("stockTransferWorkNumber", stockTransferWorkNumber);
            changeStatusAfterStartWork(stockTransferWorkNumber,scannerChosenWarehouse);
            return true;
        }
        else if( workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(stockTransferWorkNumber.toString(),scannerChosenWarehouse,SecurityUtils.username(),"Stock transfer Work") > 0){
            session.setAttribute("stockTransferWorkNumber", stockTransferWorkNumber);
            return true;
        }
        else{
            session.setAttribute("stockTransferScannerMessage","To Work number: " + stockTransferWorkNumber + " are not assigned any works to do");
            return false;
        }
    }

    @Override
    public boolean productionPickingWorkSearch(HttpSession session, Long productionNumber, String scannerChosenWarehouse) {
        if (workDetailsRepository.checkIfWorksExistsForHandleProduction(productionNumber, scannerChosenWarehouse, "Production picking") > 0) {
            session.setAttribute("productionNumberSearch", productionNumber);
            List<WorkDetails> works = workDetailsRepository.getWorkListByWarehouseAndHandle(productionNumber.toString(), scannerChosenWarehouse);
            for (WorkDetails singularWork : works) {
                singularWork.setStatus(SecurityUtils.username());
                workDetailsRepository.save(singularWork);
                log.error("Production: " + singularWork.getId() + " " + singularWork.getWorkNumber());
            }
            return true;
        } else if (workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(productionNumber.toString(), scannerChosenWarehouse, SecurityUtils.username(), "Production picking") > 0) {
            session.setAttribute("productionNumberSearch", productionNumber);
            return true;
        } else {
            session.setAttribute("manualProductionScannerMessage", "To production number: " + productionNumber + " are not assigned any works to do");
            return false;
        }
    }

    @Override
    public boolean productionProduceWorkSearch(HttpSession session, Long productionNumber, String scannerChosenWarehouse) {
        if(workDetailsRepository.checkIfWorksExistsForHandleProduction(productionNumber,scannerChosenWarehouse,"Producing finish product from collected intermediate articles")>0){
            session.setAttribute("productionNumberSearch", productionNumber);
            changeStatusAfterStartWork(productionNumber,scannerChosenWarehouse);
            return true;
        }
        else if( workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(productionNumber.toString(),scannerChosenWarehouse,SecurityUtils.username(),"Producing finish product from collected intermediate articles") > 0){
            session.setAttribute("productionNumberSearch", productionNumber);
            return true;
        }
        else{
            session.setAttribute("produceScannerMessage","To production number: " + productionNumber + " are not assigned any works to do");
            return false;
        }
    }

    @Override
    public boolean productionPutawayWorkSearch(HttpSession session, Long productionNumber, String scannerChosenWarehouse) {
        if(workDetailsRepository.checkIfWorksExistsForHandleProduction(productionNumber,scannerChosenWarehouse,"Putaway after producing")>0){
            session.setAttribute("productionNumberSearch", productionNumber);
            changeStatusAfterStartWork(productionNumber,scannerChosenWarehouse);
            return true;
        }
        else if( workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(productionNumber.toString(),scannerChosenWarehouse,SecurityUtils.username(),"Putaway after producing") > 0){
            session.setAttribute("productionNumberSearch", productionNumber);
            return true;
        }
        else{
            session.setAttribute("scannerProductionPutawayMessage","To production number: " + productionNumber + " are not assigned any works to do");
            return false;
        }
    }

    @Override
    public boolean receptionPutawayWorkSearch(HttpSession session, Long receptionNumber, String scannerChosenWarehouse) {
        if(workDetailsRepository.checkIfWorksExistsForHandle(receptionNumber.toString(),scannerChosenWarehouse)>0){
            session.setAttribute("receptionNumberSearch", receptionNumber);
            List<WorkDetails> works = workDetailsRepository.getWorkListByWarehouseAndHandle(receptionNumber.toString(),scannerChosenWarehouse);
            for(WorkDetails singularWork : works){
                singularWork.setStatus(SecurityUtils.username());
                workDetailsRepository.save(singularWork);
                log.error(singularWork.getId() + " " + singularWork.getWorkNumber());
            }
            return true;
        }
        //work for reception found with userName status
        else if( workDetailsRepository.checkIfWorksExistsForHandleWithStatusUser(receptionNumber.toString(),scannerChosenWarehouse,SecurityUtils.username()) > 0){
            session.setAttribute("receptionNumberSearch", receptionNumber);
            return true;
        }
        //work for reception not found
        else{
            session.setAttribute("manualReceptionMessage","To reception number: " + receptionNumber + " are not assigned any works to do");
            return false;
        }
    }

}
