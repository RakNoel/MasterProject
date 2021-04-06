package com.raknoel.bma.structures;

import java.util.*;

public class BinaryMatrix implements Iterable<BitSet> {
    private final BitSet[] columns;

    private final int width;
    private final int height;

    public BinaryMatrix(int width, int height) {
        columns = new BitSet[width];
        for (int i = 0; i < width; i++) this.columns[i] = new BitSet();
        this.height = height;
        this.width = width;
    }

    public BinaryMatrix(List<BitSet> columns, int height) {
        this.columns = new BitSet[columns.size()];
        for (int i = 0; i < columns.size(); i++) this.columns[i] = columns.get(i);
        this.height = height;
        this.width = columns.size();
    }

    public boolean getBit(int x, int y) {
        if (invalidPos(x, y)) throw new IndexOutOfBoundsException();
        return this.columns[x].get(y);
    }

    public BitSet getColumn(int n) {
        return this.columns[n];
    }

    public BitSet getRow(int n) {
        if (invalidPos(0, n)) throw new IndexOutOfBoundsException();
        BitSet holder = new BitSet();

        for (int i = 0; i < this.getWidth(); i++) {
            holder.set(i, this.columns[i].get(n));
        }
        return holder;
    }

    public List<BitSet> getColumns(Set<Integer> x) {
        ArrayList<BitSet> holder = new ArrayList<>(x.size());
        for (var c : x) holder.add(this.getColumn(c));

        return holder;
    }

    public List<BitSet> getRows(Set<Integer> x) {
        ArrayList<BitSet> holder = new ArrayList<>(x.size());
        for (int c : x)
            holder.add(this.getRow(c));
        return holder;
    }

    public void setBit(int x, int y, boolean value) {
        if (invalidPos(x, y))
            throw new IndexOutOfBoundsException(String.format("x: %d y: %d is outside of bounds", x, y));

        this.getColumn(x).set(y, value);
    }

    public void setColumn(int x, BitSet bts) {
        if (invalidPos(x, 0)) throw new IndexOutOfBoundsException();
        this.columns[x] = bts;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();

        Set<Integer> s = new HashSet<>();
        for (int i = 0; i < this.getHeight(); i++) s.add(i);
        for (var row : getRows(s)) {
            for (int bit = 0; bit < getWidth(); bit++)
                bld.append(row.get(bit) ? '1' : '0').append(" ");
            bld.append(System.lineSeparator());
        }
        return bld.toString();
    }

    public BinarySubMatrix getChild() {
        return new BinarySubMatrix(this);
    }

    public boolean invalidPos(int x, int y) {
        return x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight();
    }

    @Override
    public Iterator<BitSet> iterator() {
        ArrayList<BitSet> holder = new ArrayList<>(this.getWidth());
        for (int i = 0; i < this.getWidth(); i++)
            holder.add(this.getColumn(i));
        return holder.iterator();
    }
}
