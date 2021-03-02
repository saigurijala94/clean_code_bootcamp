package com.zemoso.storecheckout.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemChart {

    private String name;
    private double price;
    private String specialPrice;
}
