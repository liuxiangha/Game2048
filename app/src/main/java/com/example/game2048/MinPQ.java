package com.example.game2048;

import android.annotation.SuppressLint;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MinPQ<Key> implements Iterable<Key> {
    private Key[] pq;

    private int n;

    private Comparator<Key> comparator;

    public MinPQ(int initCapacity) {
        this.pq = (Key[])new Object[initCapacity + 1];
        this.n = 0;
    }

    public MinPQ() {
        this(1);
    }

    public MinPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        this.pq = (Key[])new Object[initCapacity + 1];
        this.n = 0;
    }

    public MinPQ(Comparator<Key> comparator) {
        this(1, comparator);
    }

    public MinPQ(Key[] keys) {
        this.n = keys.length;
        this.pq = (Key[])new Object[keys.length + 1];
        for (int i = 0; i < this.n; i++)
            this.pq[i + 1] = keys[i];
        for (int k = this.n / 2; k >= 1; k--)
            sink(k);
        assert isMinHeap();
    }

    public boolean isEmpty() {
        return (this.n == 0);
    }

    public int size() {
        return this.n;
    }

    public Key min() {
        if (isEmpty())
            throw new NoSuchElementException("Priority queue underflow");
        return this.pq[1];
    }

    @SuppressLint("Assert")
    private void resize(int capacity) {
        assert capacity > this.n;
        Key[] temp = (Key[])new Object[capacity];
        for (int i = 1; i <= this.n; i++)
            temp[i] = this.pq[i];
        this.pq = temp;
    }

    @SuppressLint("Assert")
    public void insert(Key x) {
        if (this.n == this.pq.length - 1)
            resize(2 * this.pq.length);
        this.pq[++this.n] = x;
        swim(this.n);
        assert isMinHeap();
    }

    public Key delMin() {
        if (isEmpty())
            throw new NoSuchElementException("Priority queue underflow");
        Key min = this.pq[1];
        exch(1, this.n--);
        sink(1);
        this.pq[this.n + 1] = null;
        if (this.n > 0 && this.n == (this.pq.length - 1) / 4)
            resize(this.pq.length / 2);
        assert isMinHeap();
        return min;
    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k /= 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= this.n) {
            int j = 2 * k;
            if (j < this.n && greater(j, j + 1))
                j++;
            if (!greater(k, j))
                break;
            exch(k, j);
            k = j;
        }
    }

    private boolean greater(int i, int j) {
        if (this.comparator == null)
            return (((Comparable<Key>)this.pq[i]).compareTo(this.pq[j]) > 0);
        return (this.comparator.compare(this.pq[i], this.pq[j]) > 0);
    }

    private void exch(int i, int j) {
        Key swap = this.pq[i];
        this.pq[i] = this.pq[j];
        this.pq[j] = swap;
    }

    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    private boolean isMinHeap(int k) {
        if (k > this.n)
            return true;
        int left = 2 * k;
        int right = 2 * k + 1;
        if (left <= this.n && greater(k, left))
            return false;
        if (right <= this.n && greater(k, right))
            return false;
        return (isMinHeap(left) && isMinHeap(right));
    }

    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {
        private MinPQ<Key> copy;

        public HeapIterator() {
            if (MinPQ.this.comparator == null) {
                this.copy = new MinPQ<>(MinPQ.this.size());
            } else {
                this.copy = new MinPQ<>(MinPQ.this.size(), MinPQ.this.comparator);
            }
            for (int i = 1; i <= MinPQ.this.n; i++)
                this.copy.insert((Key)MinPQ.this.pq[i]);
        }

        public boolean hasNext() {
            return !this.copy.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Key next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return this.copy.delMin();
        }
    }
}

