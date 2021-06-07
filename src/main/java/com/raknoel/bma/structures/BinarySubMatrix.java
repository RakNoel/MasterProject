package com.raknoel.bma.structures;

import java.util.*;

import static com.raknoel.bma.tools.HammingDistance.hammingDistance;

public class BinarySubMatrix implements Iterable<BitSet> {
    private final BinaryMatrix parent;

    private List<Integer> rowsCount;
    private List<Integer> columnsCount;

    private boolean finished;

    public BinarySubMatrix(BinaryMatrix parent) {
        this.parent = parent;
        this.finished = false;
        setupHeightWidth(parent.getWidth(), parent.getHeight());
    }

    public BinarySubMatrix(BinaryMatrix parent, List<Integer> columnsCount) {
        this.parent = parent;
        setupHeightWidth(parent.getWidth(), parent.getHeight());
        this.columnsCount = columnsCount;
        this.finished = false;
    }

    public int getWidth() {
        return columnsCount.size();
    }

    public int getHeight() {
        return rowsCount.size();
    }

    public void deleteColumn(int n) {
        this.columnsCount.remove(n);
    }

    public void deleteRow(int n) {
        this.rowsCount.remove(n);
    }

    public boolean getBit(int x, int y) {
        if (invalidPos(x, y)) throw new IndexOutOfBoundsException();

        var i = columnsCount.get(x);
        var j = rowsCount.get(y);
        return this.parent.getBit(i, j);
    }

    public BitSet getColumn(int n) {
        if (invalidPos(n)) throw new IndexOutOfBoundsException();
        var i = columnsCount.get(n);
        return this.parent.getColumn(i);
    }

    public int getColumnID(int n) {
        if (invalidPos(n, 0)) throw new IndexOutOfBoundsException();
        return columnsCount.get(n);
    }

    public BitSet getRow(int n) {
        if (invalidPos(0, n)) throw new IndexOutOfBoundsException();

        BitSet holder = new BitSet();
        n = rowsCount.get(n);
        int j = 0;

        for (Integer w : columnsCount) holder.set(j++, this.parent.getBit(w, n));

        return holder;
    }

    public int getRowID(int n) {
        if (invalidPos(0, n)) throw new IndexOutOfBoundsException();
        return rowsCount.get(n);
    }

    public List<BitSet> getColumns(Collection<Integer> x) {
        ArrayList<BitSet> holder = new ArrayList<>(x.size());
        for (var c : x) holder.add(this.getColumn(c));

        return holder;
    }

    public List<BitSet> getColumns() {
        ArrayList<BitSet> holder = new ArrayList<>(this.getWidth());
        for (int i = 0; i < this.getWidth(); i++) holder.add(this.getColumn(i));
        return holder;
    }

    public List<BitSet> getRows(Set<Integer> x) {
        ArrayList<BitSet> holder = new ArrayList<>(x.size());
        for (int c : x) holder.add(this.getRow(c));
        return holder;
    }

    private void setupHeightWidth(int width, int height) {
        this.rowsCount = new ArrayList<>(height);
        for (int i = 0; i < height; i++) this.rowsCount.add(i);
        this.columnsCount = new ArrayList<>(width);
        for (int i = 0; i < width; i++) this.columnsCount.add(i);
    }

    private boolean invalidPos(int x, int y) {
        return x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight();
    }

    private boolean invalidPos(int x) {
        return x < 0 || x >= this.getWidth();
    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();

        Set<Integer> s = new HashSet<>();
        for (int i = 0; i < this.rowsCount.size(); i++) s.add(i);
        for (var row : getRows(s)) {
            for (int bit = 0; bit < getWidth(); bit++)
                bld.append(row.get(bit) ? '1' : '0').append(" ");
            bld.append(System.lineSeparator());
        }
        return bld.toString();
    }

    public BinaryMatrix getParent() {
        return this.parent;
    }

    @Override
    public Iterator<BitSet> iterator() {
        ArrayList<BitSet> holder = new ArrayList<>(this.getWidth());
        for (int i = 0; i < this.getWidth(); i++)
            holder.add(this.getColumn(i));
        return holder.iterator();
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished() {
        this.finished = true;
    }

    public int totalHammingDist(List<BitSet> S) {
        if (S.isEmpty()) return Integer.MAX_VALUE;

        int dist = 0;
        for (var column : this.getColumns()) {
            int tmpDist = Integer.MAX_VALUE;
            for (var vector : S) tmpDist = Math.min(hammingDistance(column, vector), tmpDist);
            dist += tmpDist;
        }
        return dist;
    }
}
