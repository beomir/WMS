package pl.coderslab.cls_wms_app.service.wmsSettings;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Extremely;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.repository.*;

import java.util.List;

@Slf4j
@Service
public class ExtremelyServiceImpl implements ExtremelyService{
    private final ExtremelyRepository extremelyRepository;
    private final TransactionRepository transactionRepository;
    private final StockRepository stockRepository;
    private final ReceptionRepository receptionRepository;
    private final WorkDetailsRepository workDetailsRepository;
    private final ShipmentInCreationRepository shipmentInCreationRepository;

    @Autowired
    public ExtremelyServiceImpl(ExtremelyRepository extremelyRepository, TransactionRepository transactionRepository, StockRepository stockRepository, ReceptionRepository receptionRepository, WorkDetailsRepository workDetailsRepository, ShipmentInCreationRepository shipmentInCreationRepository) {
        this.extremelyRepository = extremelyRepository;
        this.transactionRepository = transactionRepository;
        this.stockRepository = stockRepository;
        this.receptionRepository = receptionRepository;
        this.workDetailsRepository = workDetailsRepository;
        this.shipmentInCreationRepository = shipmentInCreationRepository;
    }

    @Override
    public void add(Extremely extremely) {
        Transaction transaction = new Transaction();
        transaction.setWarehouse(extremely.getWarehouse());
        transaction.setCompany(extremely.getCompany());
        transaction.setTransactionGroup("Configuration");
        transaction.setTransactionDescription("Create new Gouge: " + extremely.getExtremelyName() + " with value: " + extremely.getExtremelyValue() + " for company: " + extremely.getCompany().getName() + " ,from Warehouse: " + extremely.getWarehouse().getName());
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
        extremelyRepository.save(extremely);
    }


    @Override
    public List<Extremely> getExtremely() {
        return extremelyRepository.getAllExtremelies();
    }

    @Override
    public Extremely findById(Long id) {
        return extremelyRepository.getOne(id);
    }

    @Override
    public void save(Extremely extremely) {
        extremelyRepository.save(extremely);
    }

    @Override
    public void delete(Long id) {
        Extremely extremely = extremelyRepository.getOne(id);
        Transaction transaction = new Transaction();
        transaction.setWarehouse(extremely.getWarehouse());
        transaction.setCompany(extremely.getCompany());
        transaction.setTransactionGroup("Configuration");
        transaction.setTransactionDescription("Gouge deleted: " + extremely.getExtremelyName() + " with value: " + extremely.getExtremelyValue() + " for company: " + extremely.getCompany().getName() + " ,from Warehouse: " + extremely.getWarehouse().getName());
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
        extremelyRepository.deleteById(id);
    }


    @Override
    public void edit(Extremely extremely) {
        Transaction transaction = new Transaction();
        transaction.setWarehouse(extremely.getWarehouse());
        transaction.setCompany(extremely.getCompany());
        transaction.setTransactionGroup("Configuration");
        transaction.setTransactionDescription("Gouge edited: " + extremely.getExtremelyName() + " with value: " + extremely.getExtremelyValue() + " for company: " + extremely.getCompany().getName() + " ,from Warehouse: " + extremely.getWarehouse().getName());
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
        extremelyRepository.save(extremely);
    }

    @Override
    public Long nextPalletNbr() {
        return extremelyRepository.nextPalletNbr();
    }

    @Override
    public Long nextWorkNumber() {
        return extremelyRepository.nextWorkNumber();
    }


}
