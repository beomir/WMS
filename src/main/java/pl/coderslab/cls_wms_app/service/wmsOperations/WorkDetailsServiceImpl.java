package pl.coderslab.cls_wms_app.service.wmsOperations;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.entity.WorkDetails;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.storage.StockService;

import javax.servlet.http.HttpSession;
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

    @Autowired
    public WorkDetailsServiceImpl(WorkDetailsRepository workDetailsRepository, TransactionRepository transactionRepository, StockRepository stockRepository, LocationRepository locationRepository, StockService stockService, StatusRepository statusRepository, ReceptionRepository receptionRepository, ReceptionService receptionService) {
        this.workDetailsRepository = workDetailsRepository;
        this.transactionRepository = transactionRepository;
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
        this.stockService = stockService;
        this.statusRepository = statusRepository;
        this.receptionRepository = receptionRepository;
        this.receptionService = receptionService;
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
        stockService.add(stock);
        workDetails.setStatus("close");
        workDetailsRepository.save(workDetails);
    }

    @Override
    public void workFinished(WorkDetails workDetails, HttpSession session){
        //TODO add operations which will change location free volume and free weight & transaction
        List<Stock> stock = stockRepository.getStockListByWorkNumber(Long.parseLong(workDetails.getHandle()));

        for (Stock value:stock) {
            log.error("value.getArticle().isProduction(): " + value.getArticle().isProduction());
            if(value.getArticle().isProduction() == false){
                value.setStatus(statusRepository.getStatusByStatusName("on_hand","Stock"));
            }
            else{
                log.error("status: " + statusRepository.getStatusByStatusName("on_production","Production"));
                value.setStatus(statusRepository.getStatusByStatusName("on_production","Production"));
            }
            stockRepository.save(value);
        }
        log.error("workDetailsRepository.workTypeByWorkNumber(workDetails.getWorkNumber()): " + workDetailsRepository.workTypeByWorkNumber(workDetails.getWorkNumber()));
        if(workDetailsRepository.workTypeByWorkNumber(workDetails.getWorkNumber()) == "Reception"){
            List<Reception> reception = receptionRepository.getReceptionByReceptionNumber(Long.parseLong(workDetails.getHandle()));
            for (Reception value: reception) {
                value.setStatus(statusRepository.getStatusByStatusName("closed","Reception"));
                receptionRepository.save(value);
            }
        }
        workDetailsRepository.save(workDetails);
        receptionService.finishReception(Long.parseLong(workDetails.getHandle()),session);
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
        log.error("workDetailsStatus: " + workDetailsStatus);
        return workDetailsRepository.workHeaderList(workDetailsWarehouse,workDetailsCompany,workDetailsArticle,workDetailsType,workDetailsHandle,workDetailsHandleDevice,workDetailsStatus,workDetailsLocationFrom,workDetailsLocationTo,workDetailsWorkNumber);

    }
}
