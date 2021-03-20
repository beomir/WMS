package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.ArticleTypes;
import pl.coderslab.cls_wms_app.service.storage.ArticleTypesService;
import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class ArticleTypesFixture {

    private final ArticleTypesService articleTypesService;

    private List<ArticleTypes> articleTypesList = Arrays.asList(
            new ArticleTypes(null, "Food", "Groceries, can't be mixed with other classes", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",1),
            new ArticleTypes(null, "Chemicals", "Chemicals, can be mixed with: Metal", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",23),
            new ArticleTypes(null, "Metal", "Metal, can be mixed with: Plastics, Regular,Chemicals,Electronic", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",23456),
            new ArticleTypes(null, "Plastics", "Plastic, can be mixed with: Metal,Regular,Electronic", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",3456),
            new ArticleTypes(null, "Regular", "Regular, can be mixed with: Regular,Plastic,Electronic", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",3456),
            new ArticleTypes(null, "Electronic", "Electronic, can be mixed with: Regular,Plastic,Metal", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",3456),
            new ArticleTypes(null, "HighValue", "HighValue, can't be mixed with other classes", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",7)
    );

    @Autowired
    public ArticleTypesFixture(ArticleTypesService articleTypesService) {
        this.articleTypesService = articleTypesService;
    }

    public void loadIntoDB() {
        for (ArticleTypes articleTypes : articleTypesList) {
            articleTypesService.add(articleTypes);
        }
        ArticleTypes articleTypes1 = articleTypesList.get(0);
        ArticleTypes articleTypes2 = articleTypesList.get(1);
        ArticleTypes articleTypes3 = articleTypesList.get(2);
        ArticleTypes articleTypes4 = articleTypesList.get(3);
        ArticleTypes articleTypes5 = articleTypesList.get(3);

        articleTypesService.add(articleTypes1);
        articleTypesService.add(articleTypes2);
        articleTypesService.add(articleTypes3);
        articleTypesService.add(articleTypes4);
        articleTypesService.add(articleTypes5);

    }
}
