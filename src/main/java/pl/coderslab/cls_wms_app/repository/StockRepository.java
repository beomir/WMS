package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;


import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    //checking available location for transfer
    @Query(value="select count(*) from storage inner join location l on storage.location_id = l.id where hd_number = ?1",nativeQuery = true)
    int qtyOfPalletNumber(Long hdNumber);

    @Query(value="select count(hd_number) from storage where hd_number = (select hd_number from storage where id = ?1) and location_id = (select location_id from storage where id = ?1)",nativeQuery = true)
    int qtyOfTheSamePalletNumberInOneLocation(Long stockId);

    @Query("Select distinct b from Stock b join fetch b.company c join fetch b.article a join fetch b.status s join fetch b.warehouse w JOIN fetch Users u on u.company = c.name where w.name =?1 and u.username like ?2 order by b.location.locationName,c.name, b.hd_number")
    List<Stock> getStorage(String warehouseName, String username);

    @Query("Select distinct w from Warehouse w where w.id =?1")
    List<Warehouse> getWarehouse(Long id);

    @Query("Select distinct s from Stock s left outer join fetch Reception r on s.receptionNumber = r.receptionNumber where s.company.name = ?1")
    List<Stock> getStockByCompanyName(String name);

    @Query(value="Select count(*) from storage where hd_number = ?1",nativeQuery = true)
    int checkIfHdNumberExistsOnStock(Long hd_number);

    @Query(value = "select s.id from storage s  join article a on (a.id = s.article_id) join status st on (st.id = s.status_id) join warehouse w on (w.id = s.warehouse_id) where a.article_number = ?1 and st.status = 'on_hand' and w.name = ?2 order by s.pieces_qty desc limit 1",nativeQuery = true)
    Long searchStockToSend(Long articleNumber, String warehouseName);

    @Query(value = "select s.id from storage s join article a on (a.id = s.article_id) join status st on (st.id = s.status_id) join warehouse w on (w.id = s.warehouse_id) join company c on c.id = s.company_id join receptions r on s.reception_number = r.reception_number and s.hd_number = r.hd_number where a.article_number = ?1 and st.status = 'on_hand' and w.name = ?2 and c.name = ?3 order by r.created, s.pieces_qty desc  limit 1",nativeQuery = true)
    Long searchStockToSendFIFO(Long articleNumber, String warehouseName, String companyName);

    @Query(value = "select s.id from storage s join article a on (a.id = s.article_id) join status st on (st.id = s.status_id) join warehouse w on (w.id = s.warehouse_id) join company c on c.id = s.company_id join receptions r on s.reception_number = r.reception_number and s.hd_number = r.hd_number where a.article_number = ?1 and st.status = 'on_hand' and w.name = ?2 and c.name = ?3 order by r.created, s.pieces_qty desc  limit 1",nativeQuery = true)
    Long searchStockToSendFEFO(Long articleNumber, String warehouseName,String companyName);

    @Query(value = "select s.id from storage s join article a on (a.id = s.article_id) join status st on (st.id = s.status_id) join warehouse w on (w.id = s.warehouse_id) join company c on c.id = s.company_id join receptions r on s.reception_number = r.reception_number and s.hd_number = r.hd_number where a.article_number = ?1 and st.status = 'on_hand' and w.name = ?2 and c.name = ?3 order by r.created desc, s.pieces_qty desc  limit 1",nativeQuery = true)
    Long searchStockToSendLIFO(Long articleNumber, String warehouseName,String companyName);

    @Query(value = "select distinct s.id from storage s join article a on (a.id = s.article_id) join status st on (st.id = s.status_id) join warehouse w on (w.id = s.warehouse_id) join company c on a.company_id = c.id inner join order_of_locations_header oolh on c.id = oolh.company_id inner join order_of_locations_details oold on oolh.id = oold.order_of_locations_header_id and oolh.active and s.location_id = oold.location_id join receptions r on s.reception_number = r.reception_number and s.hd_number = r.hd_number where a.article_number = ?1 and st.status = 'on_hand' and w.name = ?2 and c.name = ?3 order by oold.location_number_in_sequence, s.pieces_qty desc limit 1",nativeQuery = true)
    Long searchStockToSendOLS(Long articleNumber, String warehouseName, String companyName);

    @Query("Select s from Stock s where s.id = ?1")
    Stock getStockById(Long id);

    @Query("Select s from Stock s where s.location.locationName = ?1 and s.company.name = ?2 and s.warehouse.name = ?3")
    List<Stock> getStockByLocationNameAndCompanyNameAndWarehouseName(String locationName,String companyName,String warehouseName);

    @Query("Select s from Stock s where s.location.locationName = ?1 and s.warehouse.name = ?2")
    List<Stock> getStockContentByLocationNameAndWarehouse(String locationName,String warehouseName);

    @Query("Select s from Stock s where s.location.locationName = ?1 and s.company.name = ?2 and s.warehouse.name = ?3")
    List<Stock> getStockContentByLocationNameAndCompanyNameAndWarehouseName(String locationName,String companyName,String warehouseName);

    @Query("Select s from Stock s where s.shipmentNumber = ?1")
    List<Stock> getStockListByShipmentNumber(Long shipmentNumber);

    @Query("Select s from Stock s where s.hd_number = ?1")
    Stock getStockByHdNumber(Long hdNumber);

    @Query("Select s from Stock s where s.hd_number = ?1")
    List<Stock> getStockListByHdNumber(Long hdNumber);


    @Query("Select s from Stock s where s.hd_number = ?1 and s.article.article_number =?2 and s.location.locationName = ?3 and s.warehouse.name = ?4 and s.company.name = ?5 and s.handle = ?6")
    Stock getStockByHdNumberArticleNumberLocation(Long hdNumber, Long articleNumber, String locationName,String warehouseName, String companyName, String handle);

    @Query("Select s from Stock s where s.hd_number = ?1 and s.article.article_number =?2 and s.location.locationName = ?3 and s.warehouse.name = ?4 and s.company.name = ?5 ")
    Stock getStockForFullStockTransfer(Long hdNumber, Long articleNumber, String locationName,String warehouseName, String companyName);

    @Query("Select s from Stock s where s.article.article_number =?1 and s.warehouse.name = ?2 and s.company.name = ?3 and s.handle = ?4")
    List<Stock> getStockForTransfer(Long articleNumber, String warehouseName, String companyName, String handle);

    @Query("Select s from Stock s where s.receptionNumber = ?1")
    List<Stock> getStockListByReceptionNumber(Long receptionNumber);

    @Query(value = "select count(distinct a.article_number) from storage s inner join article a on s.article_id = a.id inner join location l on s.location_id = l.id where l.location_name = ?1",nativeQuery = true)
    int qtyOfDifferentArticleNumberInStockLocation(String locationName);

    @Query("Select s from Stock s where s.hd_number = ?1 and s.company.name = ?2 and s.warehouse.name = ?3")
    List<Stock> getStockByHdNumberAndCompanyNameAndWarehouseName(Long hdNumber,String company,String warehouseName);

    @Query("Select s from Stock s where s.hd_number = ?1 and s.company.name = ?2 and s.warehouse.name = ?3 and s.location.locationName <> ?4")
    List<Stock> getStockByHdNumberAndCompanyNameAndWarehouseNameAndWithoutLocationName(Long hdNumber,String company,String warehouseName,String locationName);

    @Query("Select s from Stock s where s.hd_number = ?1 and s.company.name = ?2 and s.warehouse.name = ?3 and s.location.locationName not in (?4,?5)")
    List<Stock> getStockByHdNumberAndCompanyNameAndWarehouseNameAndWithoutTwoLocationName(Long hdNumber,String company,String warehouseName,String locationNameFirst,String locationNameSecond);

    @Query("Select s from Stock s where s.hd_number = ?1 and s.company.name = ?2 and s.warehouse.name = ?3 and s.location.locationName = ?4")
    List<Stock> getStockByHdNumberAndCompanyNameAndWarehouseNameAndLocationName(Long hdNumber,String company,String warehouseName,String locationName);

    @Query("Select s from Stock s join WorkDetails wd on s.hd_number = wd.hdNumber and s.article = wd.article where s.handle = ?1 and wd.workDescription = ?2")
    List<Stock> getStockByWorkHandleAndWorkDescription(String handle, String workDescription);

    @Query("Select s from Stock s join WorkDetails wd on s.handle = concat(wd.workNumber,'') and s.article = wd.article where s.handle = ?1 and wd.workDescription = ?2")
    List<Stock> getStockListForFinishStockTransfer(String handle, String workDescription);

    @Query("Select s from Stock s join WorkDetails wd on s.hd_number = wd.hdNumber and s.article = wd.article where s.handle = ?1")
    List<Stock> getStockByStockHandle(String handle);

    @Query(value = "select hd_number, pieces_qty, location_name changeBy, s.created from storage s inner join location l on s.location_id = l.id inner join warehouse w on s.warehouse_id = w.id inner join article a on s.article_id = a.id company c on s.company_id = c.id where c.name = ?1 and w.name = ?2 article_number = ?3 order by s.created limit 1 ",nativeQuery = true)
    StockForProduction stockForProduction(String company,String warehouse,Long articleNumber);

    public static interface StockForProduction {
        Long getHd_number();
        Long getPieces_qty();
        String getChangeBy();
        String getCreated();
    }

    @Query(value = "Select s.id,a.article_number receptionNumber,l.location_name quality,c.name changeBy,w.name comment,s.pieces_qty pieces_qty from storage s inner join warehouse w on s.warehouse_id = w.id inner join location l on s.location_id = l.id inner join company c on s.company_id = c.id inner join article a on s.article_id = a.id where c.name = ?1 and w.name = ?2 and a.article_number = ?3 order by s.created limit 1",nativeQuery = true)
    SmallStockDataLimitedToOne smallStockDataLimitedToOne(String company,String warehouse,Long articleNumber);

    public static interface SmallStockDataLimitedToOne{
        Long getId();
        Long getReceptionNumber();
        String getQuality();
        String getChangeBy();
        String getComment();
        Long getPieces_qty();
    }


    @Query(value = "with locationNumberInSequencePosition As (select oold2.location_number_in_sequence location_number_in_sequence from location inner join storage s2 on location.id = s2.location_id inner join article a2 on s2.article_id = a2.id inner join company c2 on a2.company_id = c2.id inner join order_of_locations_header oolh2 on c2.id = oolh2.company_id and oolh2.active = true inner join order_of_locations_details oold2 on c2.id = oold2.company_id and oolh2.id = oold2.order_of_locations_header_id and location.id = oold2.location_id where location_name = ?1 and article_number = ?3 ) select distinct  oold.location_number_in_sequence from location l inner join storage s on l.id = s.location_id inner join article a on s.article_id = a.id inner join company c on a.company_id = c.id inner join order_of_locations_header oolh on c.id = oolh.company_id and oolh.active = true inner join order_of_locations_details oold on c.id = oold.company_id and oolh.id = oold.order_of_locations_header_id and l.id = oold.location_id where c.name = ?4 and article_number = ?3 and location_name != ?1 order by 1 limit ?2",nativeQuery = true)
    List<Long> locationNumberInSequencePosition(String locationName,int numberOfLocation,Long articleNumber,String companyName);

    @Query("Select s from Stock s where s.location.id = ?1 and s.company.name = ?2 and s.warehouse.name = ?3 and s.article.article_number = ?4")
    Stock getStockByLocationIdAndCompanyNameAndWarehouseNameAndArticleNumber(Long locationId,String companyName,String warehouseName,Long articleNumber);
}
