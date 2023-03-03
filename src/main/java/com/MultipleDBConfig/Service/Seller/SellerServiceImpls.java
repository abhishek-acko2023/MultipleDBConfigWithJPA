package com.MultipleDBConfig.Service.Seller;

import com.MultipleDBConfig.Dao.Seller.SellerDao;
import com.MultipleDBConfig.Repository.Seller.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerServiceImpls implements SellerService{

    @Autowired
    SellerRepository sellerRepository;
    @Override
    public ResponseEntity<Object> addSeller(SellerDao sellerDao) {
        try{
            SellerDao res = sellerRepository.save(sellerDao);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> findSellerDetails(Integer sellerId) {
        Optional<SellerDao> res = sellerRepository.findById(sellerId);
        if(res.isPresent()){
            return ResponseEntity.ok(res.get());
        }
        throw new RuntimeException("Seller Not Found");
    }

    @Override
    public ResponseEntity<Object> allSellers() {
        List<SellerDao> res = sellerRepository.findAll();
        return ResponseEntity.ok(res);
    }
}
