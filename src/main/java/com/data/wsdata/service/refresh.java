package com.data.wsdata.service;

import com.data.wsdata.model.wrapper;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class refresh {

    private final Map<wrapper, Boolean> stocksToRefresh;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static final Duration refreshRate = Duration.ofSeconds(5);


    public refresh() {
        stocksToRefresh = new HashMap<>();
        setRefresh15Min();
    }

    public boolean shouldRefresh(final wrapper stock) {
        if (!stocksToRefresh.containsKey(stock)) {
            stocksToRefresh.put(stock, false);
            return true;
        }
        return stocksToRefresh.get(stock);
    }

    private void setRefresh15Min() {
        scheduler.scheduleAtFixedRate(() ->
            stocksToRefresh.forEach((stock, value) -> {
                if (stock.getLastTime().isBefore(LocalDateTime.now().minus(refreshRate))) {
                    System.out.println("refreshing " + stock.getStock().getSymbol() + "...");
                    stocksToRefresh.remove(stock);
                    stocksToRefresh.put(stock.withLastTime(LocalDateTime.now()), true);
                }
            }), 0, 5, SECONDS);
        }
    }

