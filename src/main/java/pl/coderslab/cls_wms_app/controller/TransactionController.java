package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.*;

import java.util.List;

@Slf4j
@Controller
public class TransactionController {


    private final TransactionService transactionService;
    private final CompanyService companyService;
    private final UsersService usersService;
    private final WarehouseService warehouseService;
    public TransactionSearch transactionSearch;

    @Autowired
    public TransactionController(TransactionService transactionService, CompanyService companyService, UsersService usersService, WarehouseService warehouseService, TransactionSearch transactionSearch) {
        this.transactionService = transactionService;
        this.companyService = companyService;
        this.usersService = usersService;
        this.warehouseService = warehouseService;
        this.transactionSearch = transactionSearch;
    }

    @GetMapping("/user/transactions")
    public String list(Model model) {
        List<Transaction> transaction = transactionService.getTransactionsByAllCriteria(transactionSearch.getTransactionUser(),transactionSearch.getTransactionType(),transactionSearch.getTransactionGroup(),transactionSearch.getTransactionDateFrom(),transactionSearch.getTransactionDateTo(),transactionSearch.getWarehouse(),transactionSearch.getCompany());
        log.debug(transactionSearch.getTransactionUser() + " " + transactionSearch.getTransactionType() + " " + transactionSearch.getTransactionGroup() + " " + transactionSearch.getTransactionDateFrom() + " " + transactionSearch.getTransactionDateTo() + " " + transactionSearch.getWarehouse() + " " + transactionSearch.getCompany());
        model.addAttribute("transaction", transaction);
        model.addAttribute("transactionSearch",transactionSearch);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "transactions";
    }

    @GetMapping("/user/transactions-browser")
    public String browser(Model model) {
        model.addAttribute("transactionSearching", new TransactionSearch());
        Company companys = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        List<Company> company = companyService.getCompany();
        model.addAttribute("company", company);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        usersService.loggedUserData(model);
        return "transactions-browser";
    }

    @PostMapping("/user/transactions-browser")
    public String findTransaction(TransactionSearch transactionSearching) {
        transactionService.save(transactionSearching);
        return "redirect:/user/transactions";
    }

}
