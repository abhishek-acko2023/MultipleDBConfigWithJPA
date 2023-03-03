package com.MultipleDBConfig.Repository.Customer;

import com.MultipleDBConfig.Dao.Customer.CustomerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDao, Integer> {
}
