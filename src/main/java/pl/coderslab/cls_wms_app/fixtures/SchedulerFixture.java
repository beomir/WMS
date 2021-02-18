package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Scheduler;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.SchedulerService;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("local")
public class SchedulerFixture {
    private final SchedulerService schedulerService;
    private final CompanyService companyService;

    private List<Scheduler> schedulerList = Arrays.asList(
            new Scheduler(null, "Stock", "11:30", "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,0,null),
            new Scheduler(null, "Stock", "19:35", "MONDAY,TUESDAY,WEDNESDAY,FRIDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,0,null),
            new Scheduler(null, "Stock", "20:40", "MONDAY,FRIDAY,SUNDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,0,null),
            new Scheduler(null, "Stock", "20:17", "MONDAY,FRIDAY,SATURDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,0,null),
            new Scheduler(null, "Stock", "21:13", "TUESDAY,THURSDAY,SATURDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,0,null),
            new Scheduler(null, "Stock", "18:22", "WEDNESDAY,FRIDAY,SATURDAY",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",true,0,null)
    );

    @Autowired
    public SchedulerFixture(SchedulerService schedulerService, CompanyService companyService) {
        this.schedulerService = schedulerService;
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        for (Scheduler scheduler : schedulerList) {
            schedulerService.addFixture(scheduler);
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

        schedulerService.addFixture(scheduler1);
        schedulerService.addFixture(scheduler2);
        schedulerService.addFixture(scheduler3);
        schedulerService.addFixture(scheduler4);
        schedulerService.addFixture(scheduler5);
        schedulerService.addFixture(scheduler6);

    }
}
