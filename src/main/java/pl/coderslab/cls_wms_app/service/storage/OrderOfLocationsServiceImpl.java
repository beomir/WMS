package pl.coderslab.cls_wms_app.service.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.OrderOfLocationsDetails;
import pl.coderslab.cls_wms_app.entity.OrderOfLocationsHeader;
import pl.coderslab.cls_wms_app.repository.OrderOfLocationsHeaderRepository;
import pl.coderslab.cls_wms_app.repository.OrderOfLocationsDetailsRepository;

import java.util.List;

@Service
public class OrderOfLocationsServiceImpl implements OrderOfLocationsService{
    private final OrderOfLocationsDetailsRepository orderOfLocationsDetailsRepository;
    private final OrderOfLocationsHeaderRepository orderOfLocationsHeaderRepository;

    @Autowired
    public OrderOfLocationsServiceImpl(OrderOfLocationsDetailsRepository orderOfLocationsDetailsRepository, OrderOfLocationsHeaderRepository orderOfLocationsHeaderRepository) {
        this.orderOfLocationsDetailsRepository = orderOfLocationsDetailsRepository;
        this.orderOfLocationsHeaderRepository = orderOfLocationsHeaderRepository;
    }


    @Override
    public void addDetails(OrderOfLocationsDetails orderOfLocationsDetails) {
        orderOfLocationsDetailsRepository.save(orderOfLocationsDetails);
    }

    @Override
    public void addHeader(OrderOfLocationsHeader orderOfLocationsHeader) {
        orderOfLocationsHeaderRepository.save(orderOfLocationsHeader);
    }

    @Override
    public void deactivate(Long id) {
        OrderOfLocationsHeader orderOfLocationsHeader =  orderOfLocationsHeaderRepository.getOne(id);
        orderOfLocationsHeader.setActive(false);
        orderOfLocationsHeaderRepository.save(orderOfLocationsHeader);
    }

    @Override
    public void activate(Long id) {
        OrderOfLocationsHeader orderOfLocationsHeader =  orderOfLocationsHeaderRepository.getOne(id);
        orderOfLocationsHeader.setActive(true);
        orderOfLocationsHeaderRepository.save(orderOfLocationsHeader);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public List<OrderOfLocationsHeader> orderOfLocationsHeaderList() {
        return orderOfLocationsHeaderRepository.getOrderOfLocationsHeaderForFixture();
    }
}
