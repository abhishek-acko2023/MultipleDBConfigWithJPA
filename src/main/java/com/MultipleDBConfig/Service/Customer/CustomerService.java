package com.MultipleDBConfig.Service.Customer;

import com.MultipleDBConfig.Dao.Customer.CustomerDao;
import org.springframework.http.ResponseEntity;

public interface CustomerService {

    ResponseEntity<Object> addCustomer(CustomerDao customerDao);
    ResponseEntity<Object> findCustomerDetails(Integer customerId);

    ResponseEntity<Object> allCustomers();
}
