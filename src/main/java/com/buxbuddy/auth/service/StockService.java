package com.buxbuddy.auth.service;

import com.buxbuddy.auth.entity.StockHistory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StockService {

    List<StockHistory> getThisWeekStock(Long businessId);


}