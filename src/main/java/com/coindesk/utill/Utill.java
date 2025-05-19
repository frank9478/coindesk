package com.coindesk.utill;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Utill {
	
	 public static final Map<String, String> CURRENCY_NAME_MAP = Map.of(
	            "USD", "美元",
	            "GBP", "英鎊",
	            "EUR", "歐元"
	        );
	
	 public static String convertTime() {
		    ZonedDateTime nowInTaipei = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		    return nowInTaipei.format(formatter);
		}
}
