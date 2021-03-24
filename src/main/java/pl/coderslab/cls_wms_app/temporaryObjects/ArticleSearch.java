package pl.coderslab.cls_wms_app.temporaryObjects;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class ArticleSearch {
    public String article_number;
    public String volumeBiggerThan;
    public String volumeLowerThan;
    public String widthBiggerThan;

    public String widthLowerThan;
    public String depthBiggerThan;
    public String depthLowerThan;
    public String heightBiggerThan;

    public String heightLowerThan;
    public String weightBiggerThan;
    public String weightLowerThan;
    public String createdBy;

    public String creationDateFrom;
    public String creationDateTo;
    public String lastUpdateDateFrom;
    public String lastUpdateDateTo;

    public String company;


}
