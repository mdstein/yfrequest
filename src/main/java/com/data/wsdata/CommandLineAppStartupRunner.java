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

    // get single stock price
    void stockPrice(String stockname) throws IOException {
        System.out.println(service.findPrice(getStockWrapper(stockname)));
    }

    void printOneYearHistory(wrapper stock) throws IOException {
        System.out.println(service.oneYearHist(stock));
    }

    void entityFullDetail(wrapper stock) throws IOException {
        service.findPrice(stock);
        service.findChange200DMeanPerc(stock);
    }

    void divOutput(wrapper stock) {
        System.out.println(service.lastDividend(stock));
    }

    // get list of stock prices
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

    BigDecimal change200D(wrapper stock) throws IOException {
        return service.findChange200DMeanPerc(stock);
    }

    void printStats(wrapper stock) {
        System.out.println(service.test(stock));
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

    private final String menu = "(1) check stock information\n" +
            "(2) import stock list\n(3) exit";
    @Override
    public void run(String... args) throws Exception {
        Scanner input = new Scanner(System.in);
        boolean loop = true;
        System.out.print("\n\n");
        do {
            System.out.println(menu);
            int choice = input.nextInt();
            input.nextLine();
            if (choice > 3 || choice < 0) {
                System.out.println("invalid selection");
                continue;
            }
            switch(choice) {
                case 1: // single stock information
                    System.out.println("enter ticker: ");
                    String stock = input.nextLine();
                    printStats(getStockWrapper(stock));

                   // stockPrice(stock);
                    Thread.sleep(400);
                    System.out.println("continue? y/n");
                    if (input.next().equalsIgnoreCase("y")) {
                        continue;
                    } else {
                        System.exit(0);
                    }
                    break;
                case 2: // file import
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

                    stockPrices(a);
                    System.out.printf("%s moving perc. change (200D): ", a.get(3).getStock().getSymbol());
                    System.out.print(change200D(a.get(3)) + "%\n");
                    System.out.println("\n");
                    loop = false;
                    break;
                case 3: // exit
                    System.out.println("Exiting...");
                    System.exit(0);
            }
        } while (loop);


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
        System.out.println();


    }
}
