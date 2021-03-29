package pl.coderslab.cls_wms_app.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Extremely {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String extremely;
    private String extremelyDesc;
    private String extremelyValue;
    private String created;

    private String last_update;
    private String changeBy;

}
