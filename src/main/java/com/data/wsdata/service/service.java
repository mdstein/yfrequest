package com.data.wsdata.service;


import com.data.wsdata.model.wrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockStats;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class service {

    private final refresh refreshService;

    // SINGLE STOCK WRAPPER

    public wrapper findStock(final String ticker) {
        try {
            return new wrapper(YahooFinance.get(ticker));
        } catch (IOException e) {
            System.out.println("ERROR!");
        }
        return null;
    }

    // MULTIPLE STOCK WRAPPER

    public List<wrapper> findStocks(final List<String> tickers) {
        return tickers.stream().map(this::findStock).filter(Objects::nonNull).collect(Collectors.toList());
    }

    // STOCK PRICE

    public BigDecimal findPrice(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getPrice();
    }

    // DAY % CHANGE

    public BigDecimal findChangePerc(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getChangeInPercent();
    }

    // 200 (6.5m) DAY % CHANGE

    public BigDecimal findChange200DMeanPerc(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getChangeFromAvg200InPercent();
    }

    // YEARLY HIGH

    public BigDecimal yearHigh(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getYearHigh();
    }

    // YEARLY LOW

    public BigDecimal yearLow(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getYearLow();
    }

    // DEVIATION FROM YEARLY HIGH

    public BigDecimal yearHighDev(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getChangeFromYearHigh();
    }

    // DEVIATION FROM YEAR LOW

    public BigDecimal yearLowDev(final wrapper stock) throws IOException {
        return stock.getStock().getQuote(refreshService.shouldRefresh(stock)).getChangeFromYearLow();
    }

    // DEVIATION FROM YESTERDAY (CLOSE)

    public BigDecimal yesterdayClose(final wrapper stock) {
        return stock.getStock().getQuote().getPreviousClose();
    }

    // LAST DIVIDEND

    public StockDividend lastDividend(final wrapper stock) {
        return stock.getStock().getDividend();
    }

    // 1 YEAR HISTORICAL CSV DATA

    public List<HistoricalQuote> oneYearHist(final wrapper stock) throws IOException {
        return stock.getStock().getHistory();
    }

    // CHECK IF STOCK IS VALID

    public boolean isStockValid(final wrapper stock) {
        return stock.getStock().isValid();
    }

    //

    public StockStats test(final wrapper stock) {
        return stock.getStock().getStats();
    }
}
