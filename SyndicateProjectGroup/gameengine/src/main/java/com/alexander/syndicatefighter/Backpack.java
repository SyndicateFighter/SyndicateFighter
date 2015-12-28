package com.alexander.syndicatefighter;

import android.util.Pair;

import com.alexander.syndicatefighter.Item.Item;

import java.util.*;

/**
* Created by mohi7253 on 11/18/2015.
*/
public class Backpack {

    private List<Pair<Item, Integer>> items = new ArrayList<>();
    private List<Item> itemOrder = new ArrayList<>();

    public Backpack() {

    }

    public void useItem(int location) throws Exception {
        Pair<Item, Integer> itemWithCount = items.get(location);
        Item item = itemWithCount.first;
        Integer count = itemWithCount.second;
        if (count == 1) {
            items.remove(item);
        } else {
            items.set(location, Pair.create(item, count-1));
        }
    }

    private boolean containsItem(Item item) throws Exception {
        for(int i = 0; i < items.size(); i++) {
            if (items.get(i).first == item) {
                return true;
            }
        }
        return false;
    }

    public void addItem(Item item) throws Exception {

        if (containsItem(item)) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).first == item)
                {
                    items.set(i, Pair.create(item, items.get(i).second + 1));
                }
            }
        }
        else {
            items.add(Pair.create(item, 1));
        }

    }

    public void recycleItem(int location) throws Exception {
        Pair<Item, Integer> itemWithCount = items.get(location);
        Item item = itemWithCount.first;
        Integer count = itemWithCount.second;
        if (count == 1) {
            items.remove(item);
        } else {
            items.set(location, Pair.create(item, count - 1));
        }
    }

    public List<Pair<Item, Integer>> getItems() throws Exception {

        return this.items;
    }

    //Methods to implement later: storeToJSON() : void

}
