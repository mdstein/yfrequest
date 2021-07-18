package com.data.wsdata.service;

import com.data.wsdata.model.wrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private service stockService;

    @Test
    void invoke() throws IOException {
        final wrapper stock = stockService.findStock("AAPL");
        System.out.println(stock.getStock());

        final BigDecimal price = stockService.findPrice(stock);
        System.out.println(price);

        final BigDecimal change = stockService.findChangePerc(stock);
        System.out.println(change);

        final BigDecimal change200D = stockService.findChange200DMeanPerc(stock);
        System.out.println(change200D);
    }


    @Test
    void multiple() throws IOException, InterruptedException {
        final List<wrapper> stocks = stockService.findStocks(Arrays.asList("GOOGL","AMZN"));
        findPrices(stocks);

        Thread.sleep(16000);

        final wrapper aa = stockService.findStock("AA");
        stocks.add(aa);

        System.out.println(stockService.findPrice(aa));

        findPrices(stocks);
    }


    private void findPrices(List<wrapper> stocks) {
        stocks.forEach(stock -> {
            try {
                System.out.println(stockService.findPrice(stock));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}