package pl.coderslab.cls_wms_app.service.wmsOperations;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import java.util.List;

@Service
@Slf4j
public class ScannerServiceImpl implements ScannerService{
    private final StockRepository stockRepository;

    @Autowired
    public ScannerServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;

    }

    @Override
    public List<Stock> locationPreview(String locationName,String companyName, String warehouseName) {
        return stockRepository.getStockContentByLocationNameAndCompanyNameAndWarehouseName(locationName,companyName,warehouseName);
    }
}
