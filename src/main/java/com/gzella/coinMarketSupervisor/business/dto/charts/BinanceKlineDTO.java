package com.gzella.coinMarketSupervisor.business.dto.charts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinanceKlineDTO {
    String e;
    String E;
    String s;
    Kline k;

    @Override
    public String toString() {
        return k.toString();
    }

    @Getter
    @Setter
    public class Kline {
        String t;
        String T;
        String s;
        String i;
        String f;
        String L;
        String o;
        String c;
        String h;
        String l;
        String v;
        String n;
        String x;
        String q;
        String V;
        String Q;
        String B;

        @Override
        public String toString() {
            return "{\"time\":\"" + T + "\", " +
                    "\"open\":\"" + o + "\", " +
                    "\"high\":\"" + h + "\", " +
                    "\"low\":\"" + l + "\", " +
                    "\"close\":\"" + c + "\"}";
        }
    }
}