package com.billy.plane.entity;

public class Item_settting {

    private int itemImg;
    private String text;

    public int getItemImg() {
        return itemImg;
    }

    public void setItemImg(int itemImg) {
        this.itemImg = itemImg;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Item_settting(int itemImg, String text) {
        this.itemImg = itemImg;
        this.text = text;
    }
}
