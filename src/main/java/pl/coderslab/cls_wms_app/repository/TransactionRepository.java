package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Transaction;


import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("Select distinct t from Transaction t join fetch t.company c JOIN fetch Users u on u.company = c.name where  u.username like ?1")
    List<Transaction> getTransactionsByUser(String username);

    @Query("Select t from Transaction t")
    List<Transaction> getAllTransactions();

    @Query("Select t from Transaction t where t.transactionType = ?1")
    List<Transaction>  getTransactionsByTransactionType(int transactionType);

    @Query("Select t from Transaction t where t.transactionGroup = ?1")
    List<Transaction>  getTransactionsByTransactionGroup(String transactionGroup);

    @Query("Select t from Transaction t where t.created = ?1")
    List<Transaction>  getTransactionsByCreateDate(String creationDate);

    @Query("Select t from Transaction t where t.createdBy = ?1")
    List<Transaction>  getTransactionsByCreatedUser(String createdBy);

    @Query("Select t from Transaction t where t.createdBy like ?1 and t.transactionType like ?2 and t.transactionGroup like ?3 and t.created > ?4 and t.created < ?5 and t.warehouse.name like ?6 and t.company.name like ?7")
    List<Transaction> getTransactionByTypeAndGroupAndCreatedAndCreateBy(String createdBy, String transactionType, String transactionGroup, String transactionDateFrom, String transactionDateTo, String warehouse, String company);

}
