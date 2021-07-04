package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.Production;

import java.util.List;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {


    @Query("Select p from Production p where p.company.name like ?1 and p.warehouse.name like ?2 and CONCAT(p.finishProductNumber,'') like ?3 and CONCAT(p.intermediateArticleNumber,'') like ?4 and p.created like ?5 and p.last_update like ?6 and p.status like ?7 ")
    List<Production> getProductionByCriteria(String companyName,String warehouseName,String finishProductNumber,String intermediateArticleNumber,String created, String lastUpdate, String status);

    @Query("Select p from Production p where p.productionNumber = ?1")
    Production getProductionByNumber(Long productionNumber);

    @Query("Select p from Production p where p.productionNumber = ?1")
    List<Production> getProductionListByNumber(Long productionNumber);

    @Query(value = "Select distinct finish_product_number from production where production_number = ?1 order by 1 limit 1",nativeQuery = true)
    Long getArticleNumberByProductionNumber(Long productionNumber);

    @Query(value = "Select finish_product_pieces from production where production_number = ?1 order by 1 limit 1",nativeQuery = true)
    Long getQuantityToProduceByProductionNumber(Long productionNumber);

    @Query(value = "Select production_number from production where production_number = ?1 order by 1 limit 1",nativeQuery = true)
    Long getProductionNumberByProductionNumber(Long productionNumber);

    @Query(value = "Select production_number from Production p where  concat(production_number,'') like concat(substr(?1,1,8), '%')  order by 1 DESC limit 1",nativeQuery = true)
    Long lastProductionNumberForToday(Long productionNumber);


    @Query(value = "Select distinct production_number productionNumber,finish_product_number finishProductNumber,finish_product_pieces finishProductPieces, sum(if(p.status = 'close',0,intermediate_article_pieces)) intermediateArticlePieces,w.name changeBy,if(count(distinct p.status) > 1,'%',p.status) status,max(p.last_update) last_update from production p inner join company c on p.company_id = c.id inner join warehouse w on p.warehouse_id = w.id where c.name like ?1 and w.name like ?2 and CONCAT(p.finish_product_number,'') like ?3 and CONCAT(p.intermediate_article_number,'') like ?4 and p.created like ?5 and p.last_update like ?6 and p.status like ?7 group by production_number,finish_product_number,w.name",nativeQuery = true)
    List<ProductionHeader> getProductionHeaderByCriteria(String companyName,String warehouseName,String finishProductNumber,String intermediateArticleNumber,String created, String lastUpdate, String status);

    public static interface ProductionHeader{
        Long getProductionNumber();
        Long getFinishProductNumber();
        Long getFinishProductPieces();
        Long getIntermediateArticlePieces();
        String getChangeBy();
        String getStatus();
        String getLast_update();
    }

    @Query(value = "Select distinct production_number productionNumber,finish_product_number finishProductNumber,finish_product_pieces finishProductPieces,w.name changeBy from production p inner join warehouse w on w.id = p.warehouse_id where production_number = ?1 order by 1 limit 1",nativeQuery = true)
    ProductionHeaderTitleInfo getProductionHeaderTitleInfo(Long productionNumber);
    public static interface ProductionHeaderTitleInfo{
        Long getProductionNumber();
        Long getFinishProductNumber();
        Long getFinishProductPieces();
        String getChangeBy();
    }

    @Query(value = "select finish_product_number finishProductNumber, finish_product_pieces finishProductPieces from production where production_number = ?1 limit 1 ",nativeQuery = true)
    FinishProductSmallDataByProductionNumber getFinishProductSmallDataByProductionNumber(Long productionNumber);

    public static interface FinishProductSmallDataByProductionNumber{
        Long getFinishProductNumber();
        Long getFinishProductPieces();
    }
}
