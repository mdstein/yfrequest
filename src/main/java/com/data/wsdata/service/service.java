package com.data.wsdata.service;


import com.data.wsdata.model.wrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class service {

    private final refresh refreshService;

    public wrapper findStock(final String ticker) {
        try {
            return new wrapper(YahooFinance.get(ticker));
        } catch (IOException e) {
            System.out.println("ERROR!");
        }
        return null;
    }

    public List<wrapper> findStocks(final List<String> tickers) {
        return tickers.stream().map(this::findStock).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public BigDecimal findPrice(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getPrice();
    }

    public BigDecimal findChangePerc(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getChangeInPercent();
    }

    public BigDecimal findChange200DMeanPerc(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getChangeFromAvg200InPercent();
    }
}
