package com.gzella.coinMarketSupervisor.persistence.entity;

public enum Coin {
    USDT("/css/images/USDT.png", "USDT", "Tether"),
    BTC("/css/images/BTC.png", "BTC", "Bitcoin"),
    ETH("/css/images/ETH.png", "ETH", "Ethereum"),
    BNB("/css/images/BNB.png", "BNB", "BinanceCoin"),
    XRP("/css/images/XRP.png", "XRP", "Ripple"),
    ADA("/css/images/ADA.png", "ADA", "Cardano"),
    DOT("/css/images/DOT.png", "DOT", "Polkadot"),
    LTC("/css/images/LTC.png", "LTC", "Litecoin"),
    TRX("/css/images/TRX.png", "TRX", "Tron"),
    FIL("/css/images/FIL.png", "FIL", "Filecoin"),
    ONT("/css/images/ONT.png", "ONT", "Ontology"),
    XLM("/css/images/XLM.png", "XLM", "Stellar");

    public final String icon;
    public final String symbol;
    public final String label;

    private Coin(String icon, String symbol, String label) {
        this.icon = icon;
        this.symbol = symbol;
        this.label = label;
    }
}