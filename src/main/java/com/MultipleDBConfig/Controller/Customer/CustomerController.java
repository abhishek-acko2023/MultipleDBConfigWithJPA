package com.MultipleDBConfig.Controller.Customer;

import com.MultipleDBConfig.Dao.Customer.CustomerDao;
import com.MultipleDBConfig.Service.Customer.CustomerServiceImpls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    @Autowired
    CustomerServiceImpls customerServiceImpls;

    @PostMapping("/demo/customer/add")
    public ResponseEntity<Object> addCustomer(@RequestBody CustomerDao customerDao){
        return customerServiceImpls.addCustomer(customerDao);
    }

    @GetMapping("/demo/customer/all")
    public ResponseEntity<Object> allCustomers(){
        return customerServiceImpls.allCustomers();
    }

    @GetMapping("/demo/customer/{id}")
    public ResponseEntity<Object> customerDetails(@PathVariable(name = "id") Integer Id){
        return customerServiceImpls.findCustomerDetails(Id);
    }


}
