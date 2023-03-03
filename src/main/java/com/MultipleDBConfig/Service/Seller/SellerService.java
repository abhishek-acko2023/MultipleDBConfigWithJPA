package com.MultipleDBConfig.Service.Seller;

import com.MultipleDBConfig.Dao.Seller.SellerDao;
import org.springframework.http.ResponseEntity;

public interface SellerService {
    ResponseEntity<Object> addSeller(SellerDao sellerDao);
    ResponseEntity<Object> findSellerDetails(Integer sellerId);

    ResponseEntity<Object> allSellers();
}
