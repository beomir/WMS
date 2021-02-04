package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Scheduler;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.SchedulerService;

import java.util.Arrays;
import java.util.List;

@Component
public class SchedulerFixture {
    private final SchedulerService schedulerService;
    private final CompanyService companyService;

    private List<Scheduler> schedulerList = Arrays.asList(
            new Scheduler(null, "Stock", "30", "11", "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,null),
            new Scheduler(null, "Stock", "35", "19", "MONDAY,TUESDAY,WEDNESDAY,FRIDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,null),
            new Scheduler(null, "Stock", "40", "21", "MONDAY,FRIDAY,SUNDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,null),
            new Scheduler(null, "Stock", "35", "23", "MONDAY,FRIDAY,SATURDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,null),
            new Scheduler(null, "Stock", "21", "00", "TUESDAY,THURSDAY,SATURDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,null),
            new Scheduler(null, "Stock", "22", "03", "WEDNESDAY,FRIDAY,SATURDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,null)
    );

    @Autowired
    public SchedulerFixture(SchedulerService schedulerService, CompanyService companyService) {
        this.schedulerService = schedulerService;
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        for (Scheduler scheduler : schedulerList) {
            schedulerService.add(scheduler);
        }
        List<Company> companies = companyService.getCompany();
        Scheduler scheduler1 = schedulerList.get(0);
        Scheduler scheduler2 = schedulerList.get(1);
        Scheduler scheduler3 = schedulerList.get(2);
        Scheduler scheduler4 = schedulerList.get(3);
        Scheduler scheduler5 = schedulerList.get(4);
        Scheduler scheduler6 = schedulerList.get(5);

        scheduler1.setCompany(companies.get(0));
        scheduler2.setCompany(companies.get(1));
        scheduler3.setCompany(companies.get(2));
        scheduler4.setCompany(companies.get(3));
        scheduler5.setCompany(companies.get(4));
        scheduler6.setCompany(companies.get(5));

        schedulerService.add(scheduler1);
        schedulerService.add(scheduler2);
        schedulerService.add(scheduler3);
        schedulerService.add(scheduler4);
        schedulerService.add(scheduler5);
        schedulerService.add(scheduler6);

    }
}
