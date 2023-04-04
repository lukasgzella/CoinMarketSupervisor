package com.gzella.coinMarketSupervisor.business.dto.charts;

public enum Interval {

//    s-> seconds; m -> minutes; h -> hours; d -> days; w -> weeks; M -> months

    _1s("1s"),
    _1m("1m"),
    _3m("3m"),
    _5m("5m"),
    _15m("15m"),
    _30m("30m"),
    _1h("1h"),
    _2h("2h"),
    _4h("4h"),
    _6h("6h"),
    _8h("8h"),
    _12h("12h"),
    _1d("1d"),
    _3d("3d"),
    _1w("1w"),
    _1M("1M");

    public final String symbol;

    Interval(String symbol) {
        this.symbol = symbol;
    }
}
