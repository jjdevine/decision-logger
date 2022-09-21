package com.jonathandevinesoftware.decisionlogger.gui.utils;

import com.jonathandevinesoftware.decisionlogger.core.ApplicationConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiMap<K,V> {

    private Map<K, List<V>> map = new HashMap<>();

    public void put(K key, V value) {

        List valueList = map.get(key);

        if(valueList == null) {
            valueList = new ArrayList<>();
            map.put(key, valueList);
        }

        valueList.add(value);
    }

    public List<V> getValues(K key) {
        return map.get(key);
    }

    public List<V> getValuesOrNewList(K key) {
        List<V> list = getValues(key);
        if(list == null) {
            if(ApplicationConstants.DEBUG) {
                System.out.println("MULTIMAP WARNING - NO VALUES FOUND - " + key);
            }
            return new ArrayList<>();
        }
        return list;
    }
}
