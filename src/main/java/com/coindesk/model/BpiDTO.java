package com.coindesk.model;

import java.util.Map;

public class BpiDTO {

    private Map<String, CurrencyDTO> currencies;

    public Map<String, CurrencyDTO> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, CurrencyDTO> currencies) {
        this.currencies = currencies;
    }
}
