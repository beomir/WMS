package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.repository.ReceptionRepository;
import pl.coderslab.cls_wms_app.repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    public TransactionSearch transactionSearch;
    private final ReceptionRepository receptionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionSearch transactionSearch, ReceptionRepository receptionRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionSearch = transactionSearch;
        this.receptionRepository = receptionRepository;
    }

    @Override
    public void add(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransaction(String username) {
        return transactionRepository.getTransactionsByUser(username);
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
    public List<Transaction> getTransactionByTransactionType(int transactionType) {
        return transactionRepository.getTransactionsByTransactionType(transactionType);
    }

    @Override
    public List<Transaction> getTransactionByTransactionGroup(String transactionGroup) {
        return transactionRepository.getTransactionsByTransactionGroup(transactionGroup);
    }

    @Override
    public List<Transaction> getTransactionByCreationDate(String creationDate) {
        return transactionRepository.getTransactionsByCreateDate(creationDate);
    }

    @Override
    public List<Transaction> getTransactionsByCreatedUser(String createdBy) {
        return transactionRepository.getTransactionsByCreatedUser(createdBy);
    }

    @Override
    public List<Transaction> getTransactionsByAllCriteria(String createdBy, String transactionType, String transactionGroup, String transactionFrom, String transactionTo,String warehouse, String company) {
        if(createdBy.equals("")){
            createdBy = "%";
        }
        if(transactionType.equals("")){
            transactionType = "%";
        }
        if(transactionGroup.equals("")){
            transactionGroup = "%";
        }
        if(transactionFrom.equals("")){
            transactionFrom = "1970-01-01";
        }
        if(transactionTo.equals("")){
            transactionTo = "2222-02-02";
        }
        if(company.equals("all")){
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
