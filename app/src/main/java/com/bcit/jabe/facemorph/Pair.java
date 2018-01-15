package com.bcit.jabe.facemorph;

/**
 * Created by Xeltide on 15/01/2018.
 */

public class Pair<T> {

    Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    T first;
    T second;
}
