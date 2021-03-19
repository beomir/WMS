package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class ArticleTypes {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String articleClass;
    private String articleClassDescription;


    private String created;
    private String last_update;
    private boolean active;
    private String changeBy;
    private int mixed;


    public ArticleTypes(Long id,String articleClass,String articleClassDescription, String created, String last_update, boolean active, String changeBy,int mixed ) {
        this.id = id;
        this.articleClass = articleClass;
        this.articleClassDescription = articleClassDescription;
        this.created = created;
        this.last_update = last_update;
        this.active = active;
        this.changeBy = changeBy;
        this.mixed = mixed;
    }

    @OneToMany(mappedBy="articleTypes")
    private List<Article> articleList = new ArrayList<>();


}
