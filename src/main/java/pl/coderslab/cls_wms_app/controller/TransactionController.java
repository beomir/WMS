package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.TransactionSearch;

import java.util.List;

@Slf4j
@Controller
public class TransactionController {


    private final TransactionService transactionService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    public TransactionSearch transactionSearch;
    private UsersService usersService;

    @Autowired
    public TransactionController(TransactionService transactionService, CompanyService companyService, WarehouseService warehouseService, TransactionSearch transactionSearch, UsersService usersService) {
        this.transactionService = transactionService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.transactionSearch = transactionSearch;
        this.usersService = usersService;
    }

    @GetMapping("/user/transactions")
    public String list(Model model) {
        List<Transaction> transaction = transactionService.getTransactionsByAllCriteria(transactionSearch.getTransactionUser(),transactionSearch.getTransactionType(),transactionSearch.getTransactionGroup(),transactionSearch.getTransactionDateFrom(),transactionSearch.getTransactionDateTo(),transactionSearch.getWarehouse(),transactionSearch.getCompany());
        log.debug(transactionSearch.getTransactionUser() + " " + transactionSearch.getTransactionType() + " " + transactionSearch.getTransactionGroup() + " " + transactionSearch.getTransactionDateFrom() + " " + transactionSearch.getTransactionDateTo() + " " + transactionSearch.getWarehouse() + " " + transactionSearch.getCompany());
        model.addAttribute("transaction", transaction);
        model.addAttribute("transactionSearch",transactionSearch);
        usersService.loggedUserData(model);
        return "wmsSettings/transactions/transactions";
    }

    @GetMapping("/user/transactions-browser")
    public String browser(Model model) {
        model.addAttribute("transactionSearching", new TransactionSearch());

        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        usersService.loggedUserData(model);
        return "wmsSettings/transactions/transactions-browser";
    }

    @PostMapping("/user/transactions-browser")
    public String findTransaction(TransactionSearch transactionSearching) {
        transactionService.save(transactionSearching);
        return "redirect:/user/transactions";
    }

}
