package com.zemoso.storecheckout.service;

import com.zemoso.storecheckout.model.ItemChart;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CheckoutService {

    public void loadItemsInfoAndOffers(String pricingRule);

    public double doCheckoutProcessing(Map<String, Integer> items);

    public void startCheckoutApp();

    public Map<String, ItemChart> getItemsChartMap();

}
