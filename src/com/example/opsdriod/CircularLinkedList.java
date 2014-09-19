package com.example.opsdriod;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by chlr on 9/19/14.
 */
public class CircularLinkedList<T> {

    private AtomicInteger counter;
    private ArrayList<T> list;

    public CircularLinkedList() {
        list = new ArrayList<T>(10);
        counter = new AtomicInteger(0);
    }

    public CircularLinkedList<T> addNode(T node) {
        list.add(node);
        return this;
    }

    public T getNext() {
        return list.get(counter.incrementAndGet()%list.size());
    }

    public T getPrevious() {
        return list.get(counter.decrementAndGet()%list.size());
    }


    public T getAtIndex(int i) {
        return list.get(i);
    }


}
