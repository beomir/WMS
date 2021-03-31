package pl.coderslab.cls_wms_app.service.wmsSettings;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Extremely;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.repository.ExtremelyRepository;
import pl.coderslab.cls_wms_app.repository.TransactionRepository;

import java.util.List;

@Service
public class ExtremelyServiceImpl implements ExtremelyService{
    private final ExtremelyRepository extremelyRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public ExtremelyServiceImpl(ExtremelyRepository extremelyRepository, TransactionRepository transactionRepository) {
        this.extremelyRepository = extremelyRepository;
        this.transactionRepository = transactionRepository;
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


}
