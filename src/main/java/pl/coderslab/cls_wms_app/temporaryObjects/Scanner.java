package pl.coderslab.cls_wms_app.temporaryObjects;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class Scanner {
   public int scannerMenu;
   public int scannerReception;
   public String equipment;
   public String warehouse;
}
