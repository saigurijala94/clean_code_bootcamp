package com.zemoso.storecheckout.service.impl;

import com.zemoso.storecheckout.exception.InvalidPathAndDataException;
import com.zemoso.storecheckout.model.ItemChart;
import com.zemoso.storecheckout.service.CheckoutService;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private Map<String, ItemChart> itemsChartMap = new HashMap<>();

    public Map<String, ItemChart> getItemsChartMap() {
        return itemsChartMap;
    }

    public void loadItemsInfoAndOffers(String pricingRule) {
        try {
            Path path = Paths.get(getClass().getClassLoader().getResource(pricingRule).toURI());
            Stream<String> lines = Files.lines(path);
            itemsChartMap =
                    lines.skip(1).
                            map(s -> s.split("\\|"))
                            .collect(Collectors.toMap(s -> s[0], s -> new ItemChart(s[0], Double.parseDouble(s.length > 1 ? s[1] : ""), (s.length > 2 ? s[2] : ""))));
        } catch (Exception e) {
            throw new InvalidPathAndDataException("PLEASE PROVIDE VALID PATH AND RULES BY RESTARTING THE APP");
        }
    }

    public void startCheckoutApp() {
        System.out.println("***Welcome to GSG Mart***");
        Scanner scanner = new Scanner(System.in);
        //loading rules data
        System.out.println("Please provide pricing rule path:");
        String pricingRule = scanner.next();
        loadItemsInfoAndOffers(pricingRule);
        //
        System.out.println("please choose below options");
        System.out.println("1.process checkout");
        System.out.println("2.exit application");
        int optionChosen = scanner.nextInt();
        while (optionChosen != 2) {
            System.out.println();
            System.out.println("-------------------scanning enter items names or press EXIT to exit-------------------");
            Map<String, Integer> items = new HashMap<>();
            String item = scanner.next();
            while (!"EXIT".equalsIgnoreCase(item)) {
                items.put(item, items.getOrDefault(item, 0) + 1);
                item = scanner.next();
            }
            doCheckoutProcessing(items);
        }
    }

    //process the cart items
    public double doCheckoutProcessing(Map<String, Integer> items) {
        double totalPrice = 0;
        int index = 1;
        System.out.println("******** Items Bill ********");
        System.out.println();
        System.out.println(" Item    Qty     Price   Unit_Price         Offer");
        System.out.println("-------------------------------------------------");
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            ItemChart itemChart = getItemsChartMap().get(entry.getKey());
            if (itemChart == null)
                continue;
            String specialPrice = itemChart.getSpecialPrice();
            double perTypePrice = 0;
            if (specialPrice != null && !specialPrice.isEmpty()) {
                String[] pricesInfo = specialPrice.split(" ");
                int offerQty = Integer.parseInt(pricesInfo[0]);
                double offerPrice = Double.parseDouble(pricesInfo[2]);

                double offerVal = (entry.getValue() / offerQty) * offerPrice;
                double remainingItemsPrice = (entry.getValue() % offerQty) * itemChart.getPrice();
                perTypePrice = offerVal + remainingItemsPrice;

            } else {
                perTypePrice = entry.getValue() * itemChart.getPrice();
            }
            System.out.println(String.format("%5s %5d %10.2f %12.2f %13s", entry.getKey(), entry.getValue(), perTypePrice, itemChart.getPrice(), itemChart.getSpecialPrice()));
            totalPrice += perTypePrice;
        }
        System.out.println();
        System.out.println(String.format("Total Price : %8.2f", totalPrice));
        System.out.println();
        System.out.println("******** THANK YOU ********");
        return totalPrice;
    }

    /*public Map<String, ItemChart> loadItemsInfoAndOffersFromDB() throws URISyntaxException, IOException {

        Map<String, ItemChart> itemsChartMap = repository.findAll()
                .stream()
                .collect(Collectors.toMap(ItemChart::getName, item -> item));
        return itemsChartMap;
    }*/
}
