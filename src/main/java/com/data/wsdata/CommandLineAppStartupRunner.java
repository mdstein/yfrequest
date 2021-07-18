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
            System.out.println();
            try {
                System.out.println(stock.getStock().getSymbol());
                System.out.println("\ncurrent price: " + service.findPrice(stock) + "\n");
                System.out.println("_____________________________________\n");
            } catch (IOException e) {
                System.out.println("FAILED TO FIND STOCKS LIST!");
            }
        });
    }

    private String lastLine = "";

    public void print(String line) {
        //clear the last line if longer
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            if (temp.length() > 1)
                System.out.print("\r" + temp);
        }
        System.out.print("\r" + line);
        lastLine = line;
    }

    private byte anim;

    void animate(String line) {
        switch (anim) {
            case 1:
                print("[ -> ] " + line);
                break;
            case 2:
                print("[ --> ] " + line);
                break;
            case 3:
                print("[ ---> ] " + line);
                break;
            default:
                anim = 0;
                print("[ > ] " + line);
        }
        anim++;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("\n\n");
        System.out.println("Enter file name: ");
        String fileName = input.nextLine();
        if (fileName != null) {
            System.out.println("\n\nfile read. continuing...");
            System.out.print("\n\n");
        }
        List<wrapper> a = read(fileName);
        CommandLineAppStartupRunner clr = new CommandLineAppStartupRunner();
        System.out.print("\n\n");
        for (int i = 0; i < a.size(); i++) {
            clr.animate("Lines read: " + (i + 1));
            Thread.sleep(200);
        }
        System.out.println();
        System.out.println();
        System.out.printf("successfully read file '%s'\n", fileName);
        Thread.sleep(400);
        System.out.print("                                                      \n" +
                "                                                      \n" +
                " /$$$$$$/$$$$   /$$$$$$  /$$   /$$  /$$$$$$  /$$$$$$$ \n" +
                "| $$_  $$_  $$ |____  $$|  $$ /$$/ /$$__  $$| $$__  $$\n" +
                "| $$ \\ $$ \\ $$  /$$$$$$$ \\  $$$$/ | $$  \\ $$| $$  \\ $$\n" +
                "| $$ | $$ | $$ /$$__  $$  >$$  $$ | $$  | $$| $$  | $$\n" +
                "| $$ | $$ | $$|  $$$$$$$ /$$/\\  $$|  $$$$$$/| $$  | $$\n" +
                "|__/ |__/ |__/ \\_______/|__/  \\__/ \\______/ |__/  |__/\n" +
                "                                                      \n" +
                "                                                      \n" +
                "                                                      ");

        // do shit below this
        stockPrices(a);
    }
}
