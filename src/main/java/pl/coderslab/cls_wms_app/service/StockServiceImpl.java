package pl.coderslab.cls_wms_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.repository.StockRepository;


import java.util.ArrayList;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    private StockRepository stockRepository;
    public List<Stock> storage = new ArrayList<>();

    @Autowired
    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> getStorage(Long id) {
        return stockRepository.getStorage(id);
    }

    @Override
    public List<Warehouse> getWarehouse(Long id) {
        return stockRepository.getWarehouse(id);
    }

    @Override
    public void add(Stock stock) {
        stockRepository.save(stock);
    }

    @Override
    public Stock get(Long id) {
        return stockRepository.getOne(id);
    }

    public void delete(Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public void update(Stock stock) {
        stockRepository.save(stock);
    }


    @Override
    public Stock findById(Long id) {
        return stockRepository.getOne(id);
    }
}
