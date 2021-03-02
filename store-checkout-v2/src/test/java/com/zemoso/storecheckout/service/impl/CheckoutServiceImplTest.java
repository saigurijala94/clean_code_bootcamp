package com.zemoso.storecheckout.service.impl;


import com.zemoso.storecheckout.exception.InvalidPathAndDataException;
import com.zemoso.storecheckout.model.ItemChart;
import com.zemoso.storecheckout.service.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckoutServiceImplTest {


    private CheckoutService checkoutService;


    @BeforeEach
    public void initialize() {
        checkoutService = new CheckoutServiceImpl();
    }

    @Test
    public void loadItemsInfoAndOffers() {
        checkoutService.loadItemsInfoAndOffers("rules.text");
        var map = checkoutService.getItemsChartMap();
        assertEquals(50, map.get("A").getPrice());
        assertEquals("3 for 130", map.get("A").getSpecialPrice());
    }

    @Test
    public void loadItemsInfoAndOffersFailCase() {
        Throwable exception =
                assertThrows(InvalidPathAndDataException.class,
                        () -> {
                            checkoutService.loadItemsInfoAndOffers("rules.text1");
                        });
        assertEquals("PLEASE PROVIDE VALID PATH AND RULES BY RESTARTING THE APP", exception.getMessage());
    }

    @Test
    void doCheckoutProcessingTest() {
        checkoutService.loadItemsInfoAndOffers("rules.text");
        assertEquals(50, checkoutService.doCheckoutProcessing(Map.of("A", 1)));
        assertEquals(100, checkoutService.doCheckoutProcessing(Map.of("A", 2)));
        assertEquals(130, checkoutService.doCheckoutProcessing(Map.of("A", 3)));
        assertEquals(240, checkoutService.doCheckoutProcessing(Map.of("A", 3, "B", 3)));
        assertEquals(0, checkoutService.doCheckoutProcessing(Map.of("N", 3, "G", 3)));
    }
}