package com.MultipleDBConfig.Service.Customer;

import com.MultipleDBConfig.Dao.Customer.CustomerDao;
import com.MultipleDBConfig.Repository.Customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpls implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;
    @Override
    public ResponseEntity<Object> addCustomer(CustomerDao customerDao) {
        try{
            CustomerDao res = customerRepository.save(customerDao);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> findCustomerDetails(Integer customerId) {
            Optional<CustomerDao> res = customerRepository.findById(customerId);
            if(res.isPresent()){
                return ResponseEntity.ok(res.get());
            }
            throw new RuntimeException("Customer Not Found");

    }

    @Override
    public ResponseEntity<Object> allCustomers() {
        List<CustomerDao> res = customerRepository.findAll();
        return ResponseEntity.ok(res);
    }
}
