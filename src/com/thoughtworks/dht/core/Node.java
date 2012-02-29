package com.thoughtworks.dht.core;

import java.util.HashMap;

/* Understands an addressable shard in a distributed key value store */
public class Node<TKey, TValue> implements Comparable<Node<TKey, TValue>> {
    private final HashMap<TKey, TValue> store;
    private final double index;

    public Node(double index) {
        store = new HashMap<TKey, TValue>();
        this.index = index;
    }

    public void put(TKey key, TValue value) {
        store.put(key, value);
    }

    public TValue get(TKey key) {
        return store.get(key);
    }

    @Override
    public int compareTo(Node other) {
        if (index == other.index)
            return 0;
        else if (index > other.index)
            return 1;
        return -1;
    }

    private double getIndex(TKey key){
        return (key.hashCode() & 0xffff) / (double)0xffff;
    }

    public boolean canStore(TKey key) {
        return  getIndex(key)<=index;
    }
}
