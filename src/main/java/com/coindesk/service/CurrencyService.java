package com.coindesk.service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.coindesk.dao.CurrencyRepository;
import com.coindesk.model.BitcoinPriceResponse;
import com.coindesk.model.CurrencyDTO;
import com.coindesk.model.CurrencyEntity;
import com.coindesk.model.CurrencyInfoEntity;
import com.coindesk.model.NewInfoResponce;
import com.coindesk.model.TimeDTO;
import com.coindesk.model.TimeEntity;
import com.coindesk.model.NewApiResponse;
import com.coindesk.utill.Utill;

@Service
public class CurrencyService {
	

    private final RestTemplate restTemplate = new RestTemplate();
  
	private final String apiUrl = "https://kengp3.github.io/blog/coindesk.json";
	
    @Autowired
    private CurrencyRepository repository;
	    
    
    public NewApiResponse fetchAndTransform() {
        BitcoinPriceResponse raw = restTemplate.getForObject(apiUrl, BitcoinPriceResponse.class);
        NewApiResponse result = new NewApiResponse();
        List<NewInfoResponce> list = new ArrayList<>();
        String formattedTime = Utill.convertTime();
        result.setUpdateTime(formattedTime);
        result.setCurrencyList(list);
        
        if (raw != null && raw.getBpi() != null) {
        	
            for (Map.Entry<String, CurrencyDTO> entry : raw.getBpi().entrySet()) {
                String code = entry.getKey();
                CurrencyDTO dto = entry.getValue();
                			     
                NewInfoResponce info = new NewInfoResponce();
                info.setCode(code);
                info.setNameZh(Utill.CURRENCY_NAME_MAP.getOrDefault(code, "未知幣別"));
                info.setRate(dto.getRate_float());
                info.setUpdatedTime(formattedTime);
                list.add(info);
            }
           
            try {
            	saveNewInfo(formattedTime, list);
            	//repository.save(result);
            } catch (Exception e) {
                System.err.println("Failed to save DB: " + e.getMessage());
            }
        }

        return result;
    }


    public List<CurrencyEntity> getAll() {
        return repository.findAll();
    }

    public CurrencyEntity getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public CurrencyEntity create(CurrencyDTO dto) {
        CurrencyEntity entity = new CurrencyEntity();
        entity.setCode(dto.getCode());
        entity.setSymbol(dto.getSymbol());
        entity.setRate(dto.getRate());
        entity.setDescription(dto.getDescription());
        entity.setRate_float(dto.getRate_float());
        return repository.save(entity);
    }
    
    


    public CurrencyEntity update(Long id, CurrencyDTO dto) {
        CurrencyEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entity not found"));
        
        entity.setCode(dto.getCode());
        entity.setSymbol(dto.getSymbol());
        entity.setRate(dto.getRate());
        entity.setDescription(dto.getDescription());
        entity.setRate_float(dto.getRate_float());
        
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    //顯示原api並 insert db
    public BitcoinPriceResponse fetchAndSaveBitcoinPrices() {
        BitcoinPriceResponse response = restTemplate.getForObject(apiUrl, BitcoinPriceResponse.class);

        if (response != null && response.getBpi() != null) {
            for (Map.Entry<String, CurrencyDTO> entry : response.getBpi().entrySet()) {
                String currencyCode = entry.getKey();
                CurrencyDTO priceData = entry.getValue();
                
                    if (response.getTime() != null) {
                        try {
                            saveTime(response.getTime());
                            //repository.save(response);
                        } catch (Exception e) {
                            System.err.println("Time save failed: " + e.getMessage());
                        }
                    }
                    
                if (priceData != null) {
                    CurrencyEntity price = new CurrencyEntity();
                    price.setCode(currencyCode);
                    price.setSymbol(priceData.getSymbol());
                    price.setRate(priceData.getRate());
                    price.setDescription(priceData.getDescription());
                    price.setRate_float(priceData.getRate_float());

                    try {
                    	saveCurrency(price);
                    	//repository.save(price);
                    } catch (Exception e) {
                        // Log 錯誤，但不讓整個流程失敗
                        System.err.println("Save failed for " + currencyCode + ": " + e.getMessage());
                    }
                }
            }
        }

        return response;
    }

    public CurrencyEntity saveCurrency(CurrencyEntity currency) {
        try {
            // 根據 code 查詢是否已存在資料
            CurrencyEntity existing = repository.findByCode(currency.getCode());

            if (existing != null) {
                // 已存在，更新欄位
                existing.setSymbol(currency.getSymbol());
                existing.setRate(currency.getRate());
                existing.setDescription(currency.getDescription());
                existing.setRate_float(currency.getRate_float());

                // 呼叫 save 進行更新
                return repository.save(existing);
            } else {
                // 不存在，新增資料
                return repository.save(currency);
            }
        } catch (Exception e) {
            // 例外可自訂義處理方式，例如 log 錯誤訊息
            System.err.println("Error saving currency: " + e.getMessage());
            throw e; // 也可以選擇往上拋出
        }
    }
    

    public void saveTime(TimeDTO timeDTO) {
        
    	TimeEntity entity = new TimeEntity();
        entity.setUpdated(timeDTO.getUpdated());
        entity.setUpdatedISO(timeDTO.getUpdatedISO());
        entity.setUpdateduk(timeDTO.getUpdateduk());

        repository.save(entity);
    }

    public BitcoinPriceResponse fetchBitcoinPrice() {
        return restTemplate.getForObject(apiUrl, BitcoinPriceResponse.class);
    }
    
	public void saveNewInfo(String updateTime, List<NewInfoResponce> list) {
	    	
	        for (NewInfoResponce info : list) {
	        	CurrencyInfoEntity entity = new CurrencyInfoEntity();
	            entity.setCode(info.getCode());
	            entity.setNameZh(info.getNameZh());
	            entity.setRate(info.getRate());
	            entity.setUpdatedTime(updateTime);
	
	            try {
	                repository.save(entity);
	            } catch (Exception e) {
	                System.err.println("Failed to save currency info for: " + info.getCode() + ", reason: " + e.getMessage());
	            }
	        }
	    }
}

