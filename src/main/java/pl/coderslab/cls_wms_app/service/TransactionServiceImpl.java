package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    public TransactionSearch transactionSearch;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionSearch transactionSearch) {
        this.transactionRepository = transactionRepository;
        this.transactionSearch = transactionSearch;
    }

    @Override
    public void add(Transaction transaction) {
        transactionRepository.save(transaction);
    }


    @Override
    public List<Transaction> getTransaction() {
        return transactionRepository.getAllTransactions();
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.getOne(id);
    }


    @Override
    public List<Transaction> getTransactionsByAllCriteria(String createdBy, String transactionType, String transactionGroup, String transactionFrom, String transactionTo,String warehouse, String company) {
        if(createdBy == null || createdBy.equals("")){
            createdBy = "%";
        }
        if(transactionType == null || transactionType.equals("")){
            transactionType = "%";
        }
        if(transactionGroup == null || transactionGroup.equals("")){
            transactionGroup = "%";
        }
        if(transactionFrom == null || transactionFrom.equals("")){
            transactionFrom = "1970-01-01";
        }
        if(transactionTo == null || transactionTo.equals("")){
            transactionTo = "2222-02-02";
        }
        if(company == null || company.equals("all")){
            company = "%";
        }
        return transactionRepository.getTransactionByTypeAndGroupAndCreatedAndCreateBy(createdBy,transactionType,transactionGroup,transactionFrom,transactionTo,warehouse,company);
    }

    @Override
    public void save(TransactionSearch transactionSearching) {
        transactionSearch.setTransactionGroup(transactionSearching.getTransactionGroup());
        transactionSearch.setTransactionType(transactionSearching.getTransactionType());
        transactionSearch.setTransactionUser(transactionSearching.getTransactionUser());
        transactionSearch.setTransactionDateFrom(transactionSearching.getTransactionDateFrom());
        transactionSearch.setTransactionDateTo(transactionSearching.getTransactionDateTo());
        transactionSearch.setCompany(transactionSearching.getCompany());
        transactionSearch.setWarehouse(transactionSearching.getWarehouse());
    }



}
