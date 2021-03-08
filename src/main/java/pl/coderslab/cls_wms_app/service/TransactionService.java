package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Transaction;

import java.util.List;

public interface TransactionService {

    void add(Transaction transaction);

    List<Transaction> getTransaction(String username);

    List<Transaction> getTransaction();

    Transaction findById(Long id);

    List<Transaction> getTransactionByTransactionType(int transactionType);

    List<Transaction> getTransactionByTransactionGroup(String transactionGroup);

    List<Transaction> getTransactionByCreationDate(String creationDate);

    List<Transaction> getTransactionsByCreatedUser(String createdBy);

    List<Transaction> getTransactionsByAllCriteria(String createdBy, String transactionType, String transactionGroup, String transactionFrom, String transactionTo,String warehouse, String company);

    void save(TransactionSearch transactionSearch);


}
