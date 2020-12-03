//package pl.coderslab.cls_wms_app.service;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import pl.coderslab.cls_wms_app.entity.Company;
//import pl.coderslab.cls_wms_app.entity.UsersDetails;
//import pl.coderslab.cls_wms_app.repository.UsersDetailsRepository;
//
//import java.util.List;
//
//@Service
//public class UsersDetailsServiceImpl implements UsersDetailsService{
//    private final UsersDetailsRepository usersDetailsRepository;
//
//    @Autowired
//    public UsersDetailsServiceImpl(UsersDetailsRepository usersDetailsRepository) {
//        this.usersDetailsRepository = usersDetailsRepository;
//
//    }
//
//
//
//    @Override
//    public void add(UsersDetails usersDetails) {
//        usersDetailsRepository.save(usersDetails);
//    }
//
////    @Override
////    public void edit(long id, String phone) {
////        usersRepository(id, phone);
////    }
//
//    @Override
//    public List<UsersDetails> getUsersDetails() {
//        return usersDetailsRepository.getUsersDetails();
//    }
//
//    @Override
//    public List<Company> getCompanyByUsersDetails() {
//        return usersDetailsRepository.getCompanyByUsersDetails();
//    }
//
//
//    @Override
//    public UsersDetails findById(Long id) {
//        return usersDetailsRepository.getOne(id);
//    }
//
//
//    @Override
//    public void delete(Long id) {
//        usersDetailsRepository.deleteById(id);
//    }
//
//    @Override
//    public void update(UsersDetails usersDetails) {
//
//    }
//}
