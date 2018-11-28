package com.example.android.shokkaku;

public class Vegetables extends food {
    private String judge;
    private  String name;
    private int price;
    private  String shopName;

    public Vegetables(String name, int price, String shopName, String judge){
        this.name = name;
        this.price = price;
        this.shopName = shopName;
        this.judge = judge;
    }
    public String getShopName() { return shopName; }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public String getJudge() {
        return judge;
    }

}
