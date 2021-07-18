package com.data.wsdata.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import yahoofinance.Stock;

import java.time.LocalDateTime;

@Getter
@With
@AllArgsConstructor
public class wrapper {

    private final Stock stock;
    private final LocalDateTime lastTime;

    public wrapper(final Stock stock) {
        this.stock = stock;
        lastTime = LocalDateTime.now();
    }
}
