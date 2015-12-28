package com.alexander.syndicatefighter.Item;

/**
 * Created by mohi7253 on 11/18/2015.
 */
public abstract class Item {
    private String itemName;

    private int itemID;

    public String getName() {
        return this.itemName;
    }

    public int getItemID()
    {
        return this.itemID;
    }
}
