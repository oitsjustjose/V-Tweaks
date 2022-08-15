package com.oitsjustjose.vtweaks.common.util;

import com.oitsjustjose.vtweaks.VTweaks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class WeightedCollection<T> {
    private final HashMap<T, Integer> items;
    final Random random;

    public WeightedCollection() {
        this.items = new HashMap<>();
        this.random = new Random();
    }

    public void clear() {
        this.items.clear();
    }

    public void add(T item, Integer chance) {
        this.items.put(item, chance);
    }

    public Set<T> getCollection() {
        return this.items.keySet();
    }

    @SuppressWarnings("unchecked")
    public HashMap<T, Integer> getWeightMap() {
        return (HashMap<T, Integer>) this.items.clone();
    }

    @Nullable
    public T pick() {
        int total = 0;
        for (Integer x : items.values()) {
            total += x;
        }

        if (total <= 0) {
            return null;
        }

        int rng = this.random.nextInt(total);
        for (Map.Entry<T, Integer> entry : this.items.entrySet()) {
            int wt = entry.getValue();
            if (rng < wt) {
                return entry.getKey();
            }
            rng -= wt;
        }

        VTweaks.getInstance().LOGGER.error("Could not reach decision on WeightedCollection#pick");
        return null;
    }
}
