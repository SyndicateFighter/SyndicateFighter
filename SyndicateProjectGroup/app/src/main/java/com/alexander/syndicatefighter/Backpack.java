package com.alexander.syndicatefighter;

import com.alexander.syndicatefighter.Item.Item;

import java.util.*;

/**
* Created by mohi7253 on 11/18/2015.
*/
public class Backpack {

    private Map<Item, Integer> items = new HashMap<Item, Integer>();

    public Backpack() {

    }

    public void useItem(Item item) throws Exception {
        int countOfItem = items.get(item);
        if (countOfItem == 1) {
            items.remove(item);
        } else {
            items.put(item, (countOfItem -1));
        }
    }

    public void addItem(Item item) throws Exception {
        if (items.containsKey(item)) {
            int countOfItem = items.get(item);
            if (countOfItem == 0) {
                items.put(item, 1);
            } else {
                items.put(item, countOfItem + 1);
            }
        }
        else {
            items.put(item, 1);
        }

    }

    public void recycleItem(Item item) throws Exception {
        //This will be more meaningful later.
        int countOfItem = items.get(item);
        if (countOfItem == 1) {
            items.remove(item);
        } else {
            items.put(item, (countOfItem -1));
        }
    }

    public Map<Item, Integer> getItems() throws Exception {

        return this.items;
    }

    //Methods to implement later: storeToJSON() : void

}
