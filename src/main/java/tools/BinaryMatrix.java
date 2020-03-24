package tools;

import java.util.*;

public class BinaryMatrix {
    private final BitSet[] columns;

    private ArrayList<Integer> rowsCount;
    private ArrayList<Integer> columnsCount;

    private final int width, height;

    public static int hammingDistance(BitSet a, BitSet b) {
        BitSet r = new BitSet();
        r.or(a);
        r.xor(b);
        return r.cardinality();
    }

    public BinaryMatrix(int width, int height) {
        columns = new BitSet[width];
        for (int i = 0; i < width; i++) this.columns[i] = new BitSet();
        setupHeightWidth(width, height);
        this.height = height;
        this.width = width;
    }

    public BinaryMatrix(ArrayList<BitSet> columns, int height) {
        this.columns = new BitSet[columns.size()];
        setupHeightWidth(columns.size(), height);
        for (int i = 0; i < columns.size(); i++) this.columns[i] = columns.get(i);
        this.height = height;
        this.width = columns.size();
    }

    public boolean getBit(int x, int y) {
        if (invalidPos(x, y)) throw new IndexOutOfBoundsException();

        var i = columnsCount.get(x);
        var j = columnsCount.get(y);
        return this.columns[i].get(j);
    }

    public BitSet getColumn(int n) {
        if (invalidPos(n, 0)) throw new IndexOutOfBoundsException();
        var i = columnsCount.get(n);
        return this.columns[i];
    }

    public BitSet getRow(int n) {
        if (invalidPos(0, n)) throw new IndexOutOfBoundsException();
        BitSet holder = new BitSet();
        n = rowsCount.get(n);
        for (int i = 0, j = 0; i < columnsCount.size(); i++) {
            var w = this.columnsCount.get(i);
            holder.set(j++,this.columns[w].get(n));
        }
        return holder;
    }

    public void deleteColumn(int n) {
        this.columnsCount.remove(n);
    }

    public void deleteRow(int n) {
        this.rowsCount.remove(n);
    }

    public ArrayList<BitSet> getColumns(Set<Integer> x) {
        ArrayList<BitSet> holder = new ArrayList<>(x.size());
        for (var c : x) holder.add(this.getColumn(c));

        return holder;
    }

    public ArrayList<BitSet> getRows(Set<Integer> x) {
        ArrayList<BitSet> holder = new ArrayList<>(x.size());
        for (int c : x) holder.add(this.getRow(c));
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

    public int getOriginalWidth() {
        return this.width;
    }

    public int getOriginalHeight() {
        return this.height;
    }

    public int getWidth() {
        return columnsCount.size();
    }

    public int getHeight() {
        return rowsCount.size();
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

    private boolean invalidPos(int x, int y) {
        return x < 0 || y < 0 || x >= this.getOriginalWidth() || y >= this.getOriginalHeight();
    }

    private void setupHeightWidth(int width, int height) {
        this.rowsCount = new ArrayList<>(height);
        for (int i = 0; i < height; i++) this.rowsCount.add(i);
        this.columnsCount = new ArrayList<>(width);
        for (int i = 0; i < width; i++) this.columnsCount.add(i);
    }

}
