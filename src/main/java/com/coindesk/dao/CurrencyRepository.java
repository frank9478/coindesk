package com.coindesk.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coindesk.model.BitcoinPriceResponse;
import com.coindesk.model.CurrencyEntity;
import com.coindesk.model.CurrencyInfoEntity;
import com.coindesk.model.NewApiResponse;
import com.coindesk.model.TimeEntity;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
	
	CurrencyEntity findByCode(String code);

	void save(CurrencyInfoEntity entity);

	void save(TimeEntity entity);

	void save(NewApiResponse result);

	void save(BitcoinPriceResponse response);

	
}
