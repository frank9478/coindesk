package com.coindesk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coindesk.model.BitcoinPriceResponse;
import com.coindesk.model.CurrencyDTO;
import com.coindesk.model.CurrencyEntity;
import com.coindesk.model.NewApiResponse;
import com.coindesk.service.CurrencyService;

@RestController
@RequestMapping("/api") 
public class BitcoinController {
		
    @Autowired
    private CurrencyService service;
    
    //call 原始api
    @GetMapping("/bitcoin")
    public BitcoinPriceResponse fetchBitcoinPrice() {
    	 return service.fetchBitcoinPrice();
    }
    
	//寫進db
    @PostMapping("/bitcoin/insert")
    public BitcoinPriceResponse getBitcoin() {
       return service.fetchAndSaveBitcoinPrices();
    }

    @PostMapping("/newapi")
    public NewApiResponse getCustomBitcoinInfo() {
        return service.fetchAndTransform();
    }
	

    //id蒐資料
    @GetMapping("/bitcoin/{id}")
    public CurrencyEntity getById(@PathVariable Long id) {
        return service.getById(id);
    }
    

    //全搜
    @GetMapping("/bitcoin/all")
    public List<CurrencyEntity> getAll() {
        return service.getAll();
    }
    
    //新增資料
    @PostMapping("/bitcoin/insertCurency")
    public CurrencyEntity create(@RequestBody CurrencyDTO  CurrencyDTO) {
        return service.create(CurrencyDTO);
    }
    
    //修改資料
    @PutMapping("/bitcoin/{id}")
    public CurrencyEntity update(
            @PathVariable Long id,
            @RequestBody CurrencyDTO CurrencyDTO)          // ← 用 DTO 收 JSON
    {
        return service.update(id, CurrencyDTO);           // ← 交給 Service 做轉換與儲存
    }
    
    
    //刪除資料
    @DeleteMapping("/bitcoin/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    
	    	


    
}
