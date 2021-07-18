package com.data.wsdata;

import com.data.wsdata.model.wrapper;
import com.data.wsdata.service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import yahoofinance.Stock;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    private service service;

    List<wrapper> read(String filename) throws FileNotFoundException {
        File file = new File(filename);
        ArrayList<String> stockList = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {

            String line = bfr.readLine();
            while (line != null) {
                stockList.add(line);
                line = bfr.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!");
            throw e;
        } catch (IOException e) {
            System.out.println("General IOEx. OCCURRED!");
        }

        final List<wrapper> scanned = service.findStocks(stockList);

        return scanned;
    }

    wrapper getStockWrapper(String stockname) throws IOException {
        final wrapper stock = service.findStock(stockname);
        return stock;
    }

    void stockPrice(String stockname) throws IOException {
        System.out.println(service.findPrice(getStockWrapper(stockname)));
    }

    void stockPrices(List<wrapper> stocks) {
        stocks.forEach(stock -> {
            System.out.println(stock.getStock().getSymbol());
            /*try {
                System.out.println(stock.getStock().getSymbol());
                //System.out.print(" : " + service.findPrice(stock));
            } catch (IOException e) {
                System.out.println("FAILED TO FIND STOCKS LIST!");
            }*/
        });
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter file name: ");
        String fileName = input.nextLine();
        if (fileName != null) {
            System.out.println("file read. continuing...");
        }

        stockPrices(read(fileName));
    }
}
