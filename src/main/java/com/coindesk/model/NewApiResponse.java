package com.coindesk.model;

import java.util.List;

public class NewApiResponse {
    
	private String updateTime;
    
	private List<NewInfoResponce> currencyList;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<NewInfoResponce> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<NewInfoResponce> currencyList) {
        this.currencyList = currencyList;
    }
}