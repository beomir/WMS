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

    @Autowired
    public WorkDetailsServiceImpl(WorkDetailsRepository workDetailsRepository, TransactionRepository transactionRepository, StockRepository stockRepository, LocationRepository locationRepository, StockService stockService, StatusRepository statusRepository, ReceptionRepository receptionRepository, ReceptionService receptionService, ArticleRepository articleRepository, ProductionRepository productionRepository, LocationService locationService) {
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
    public List<WorkDetails> getWorkDetailsPerWarehouse(Long warehouseId) {
        return workDetailsRepository.getWorkDetailsPerWarehouse(warehouseId);
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
    public void pickUpGoods(String fromLocation, String enteredArticle, String enteredHdNumber, String equipment, String warehouse, String company) {
        log.error("fromLocation: " + fromLocation);
        log.error("enteredArticle: " + enteredArticle);
        log.error("enteredHdNumber: " + enteredHdNumber);
        log.error("equipment: " + equipment);
        log.error("warehouse: " + warehouse);
        log.error("company: " + company);
        Stock stock = stockRepository.getStockByHdNumberArticleNumberLocation(Long.parseLong(enteredHdNumber),Long.parseLong(enteredArticle),fromLocation,warehouse,company);
        stock.setLocation(locationRepository.findLocationByLocationName(equipment,warehouse));
        stockService.add(stock);
    }

    @Override
    public void workLineFinish(WorkDetails workDetails,String scannerChosenEquipment){
        //TODO add operations which will change location free volume and free weight & transaction

        Stock stock = stockRepository.getStockByHdNumberArticleNumberLocation(workDetails.getHdNumber(),workDetails.getArticle().getArticle_number(),scannerChosenEquipment,workDetails.getWarehouse().getName(),workDetails.getCompany().getName());
        stock.setLocation(locationRepository.findLocationByLocationName(workDetails.getToLocation().getLocationName(),workDetails.getWarehouse().getName()));
        if(workDetails.getWorkDescription().equals("Putaway after producing")){
            stock.setStatus(statusRepository.getStatusByStatusName("on_hand","Stock"));
        }
        stockService.add(stock);
        workDetails.setStatus("close");
        workDetailsRepository.save(workDetails);
    }

    @Override
    public void workFinished(WorkDetails workDetails, HttpSession session){
        //TODO add operations which will change location free volume and free weight & transaction
        List<Stock> stock = stockRepository.getStockListByWorkNumber(Long.parseLong(workDetails.getHandle()));

        for (Stock value:stock) {
            if(value.getArticle().isProduction() == false){
                value.setStatus(statusRepository.getStatusByStatusName("on_hand","Stock"));
            }
            else{
                log.error("status after finish works: " + statusRepository.getStatusByStatusName("on_production","Production"));
                value.setStatus(statusRepository.getStatusByStatusName("on_production","Production"));
            }
            stockRepository.save(value);
        }
        log.error("workDetailsRepository.workTypeByWorkNumber(workDetails.getWorkNumber()): " + workDetailsRepository.workTypeByWorkNumber(workDetails.getWorkNumber()));
        if(workDetailsRepository.workTypeByWorkNumber(workDetails.getWorkNumber()).equals("Reception")){
            List<Reception> reception = receptionRepository.getReceptionByReceptionNumber(Long.parseLong(workDetails.getHandle()));
            for (Reception value: reception) {
                value.setStatus(statusRepository.getStatusByStatusName("closed","Reception"));
                receptionRepository.save(value);
                receptionService.finishReception(Long.parseLong(workDetails.getHandle()),session);
            }
        }
        workDetailsRepository.save(workDetails);
        if(workDetails.getWorkType().equals("Production") && workDetails.getWorkDescription().equals("Production picking")){
            WorkDetails productionWork = new WorkDetails();
            productionWork.setWorkType("Production");
            int workNumberForNewProductionWork = 100;
            while(workDetailsRepository.checkIfWorksExistsOnlyByHandle(workDetails.getHandle() + workNumberForNewProductionWork) != 0){
                workNumberForNewProductionWork++;
                log.debug("workNumberForNewProductionWork: " + workNumberForNewProductionWork);
                log.debug("workDetails.getHandle(): " + workDetails.getHandle());
                log.debug("workDetails.getHandle() + workNumberForNewProductionWork: " + workDetails.getHandle() + workNumberForNewProductionWork);
                log.debug("query result: " + workDetailsRepository.checkIfWorksExistsOnlyByHandle(workDetails.getHandle() + workNumberForNewProductionWork));
            }
            productionWork.setWorkNumber(workDetails.getWorkNumber() + workNumberForNewProductionWork);
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
            int hdNumberCounter = 1;
            while(stockRepository.checkIfHdNumberExistsOnStock(workDetails.getHdNumber() + hdNumberCounter) != 0){
                hdNumberCounter++;
            }
            productionWork.setHdNumber(workDetails.getHdNumber() + hdNumberCounter);
            workDetailsRepository.save(workDetails);
            workDetailsRepository.save(productionWork);
            session.setAttribute("productionScannerMessage","Work: " + workDetails.getWorkNumber() + " finished. Goods are available to produce on location: " + workDetails.getToLocation().getLocationName() + " by producing work: " +  productionWork.getWorkNumber());
            List<Production> productionList = productionRepository.getProductionListByNumber(workDetails.getWorkNumber());
            Long sumOfIntermediateArticlePieces = 0L;
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
    }

    @Override
    public List<WorkDetails> getWorkDetailsByCriteria(String workDetailsWarehouse, String workDetailsCompany, String workDetailsArticle, String workDetailsType,String workDetailsHandle,String workDetailsHandleDevice,String workDetailsStatus,String workDetailsLocationFrom,String workDetailsLocationTo,String workDetailsWorkNumber){
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
            return workDetailsRepository.getWorkDetailsByCriteria(workDetailsWarehouse,workDetailsCompany,workDetailsArticle,workDetailsType,workDetailsHandle,workDetailsHandleDevice,workDetailsStatus,workDetailsLocationFrom,workDetailsLocationTo,workDetailsWorkNumber);
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
            while(workDetailsRepository.checkIfWorksExistsForHandleProduction(finalWorkNumber,singularWorkToConfirm.getWarehouse().getName(),singularWorkToConfirm.getWorkDescription())!=0){
                workNumberCounter++;
                finalWorkNumber = Long.parseLong(singularWorkToConfirm.getWorkNumber() + StringUtils.leftPad(String.valueOf(workNumberCounter),2, "0"));
                log.debug("finalWorkNumber inside loop: " + finalWorkNumber);
            }
            log.debug("finalWorkNumber after loop: " + finalWorkNumber);
            putAwayAfterProduction.setWorkNumber(finalWorkNumber);
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

}
