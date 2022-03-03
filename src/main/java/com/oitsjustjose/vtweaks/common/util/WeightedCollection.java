package com.oitsjustjose.vtweaks.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WeightedArrayList<T, Integer> {
    private HashMap<T, Integer> items;
    Random random;

    public WeightedArrayList() {
        this.items = new HashMap<>();
        this.random = new Random();
    }

    public void clear() {
        this.items.clear();
    }

//    public ArrayList<T> getArrayList() {
//        return (ArrayList<T>) this.items.clone();
//    }
//

    public void add(T item, Integer chance) {
        this.items.put(item, chance);
    }

    public T pick() {
        int total = 0;
        for (Integer x : items.values()) {
            total += (int) x;
        }

        int rng =
    }
}
