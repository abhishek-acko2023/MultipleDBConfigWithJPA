package com.MultipleDBConfig.Controller.Seller;

import com.MultipleDBConfig.Dao.Seller.SellerDao;
import com.MultipleDBConfig.Service.Seller.SellerServiceImpls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SellerController {

    @Autowired
    SellerServiceImpls sellerServiceImpls;

    @PostMapping("/demo/seller/add")
    public ResponseEntity<Object> addSeller(@RequestBody SellerDao sellerDao){
        return sellerServiceImpls.addSeller(sellerDao);
    }

    @GetMapping("/demo/seller/all")
    public ResponseEntity<Object> allSellers(){
        return sellerServiceImpls.allSellers();
    }

    @GetMapping("/demo/seller/{id}")
    public ResponseEntity<Object> sellerDetails(@PathVariable(name = "id") Integer Id){
        return sellerServiceImpls.findSellerDetails(Id);
    }


}
