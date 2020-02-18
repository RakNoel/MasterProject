package tools;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Set;

/**
 * This will create a system where the columns are faster to extract than the rows.
 */
public class BinaryMatrix {
    BitSet[] columns;
    int width, height;

    public BinaryMatrix(int width, int height) {
        columns = new BitSet[width];
        for (int i = 0; i < width; i++)
            this.columns[i] = new BitSet();
        this.width = width;
        this.height = height;
    }

    public boolean getBit(int x, int y) {
        if (!validPos(x, y)) throw new IndexOutOfBoundsException();
        return this.columns[x].get(y);
    }

    public BitSet getColumn(int n) {
        if (!validPos(n, 0)) throw new IndexOutOfBoundsException();
        return this.columns[n];
    }

    public BitSet getRow(int n) {
        if (!validPos(0, n)) throw new IndexOutOfBoundsException();
        BitSet holder = new BitSet();
        for (int i = 0; i < columns.length; i++)
            holder.set(i, this.columns[i].get(n));
        return holder;
    }

    public ArrayList<BitSet> getColumns(Set<Integer> x) {
        ArrayList<BitSet> holder = new ArrayList<>(x.size());
        for (var c : x) holder.add(this.columns[c]);
        return holder;
    }

    public ArrayList<BitSet> getRows(Set<Integer> x) {
        ArrayList<BitSet> holder = new ArrayList<>(x.size());
        for (var c : x) holder.add(this.getRow(c));
        return holder;
    }

    public void setBit(int x, int y, boolean value) {
        if (!validPos(x, y)) throw new IndexOutOfBoundsException();
        this.columns[x].set(y, value);
    }

    public void setColumn(int x, BitSet bts) {
        if (!validPos(x, 0)) throw new IndexOutOfBoundsException();
        this.columns[x] = bts;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    private boolean validPos(int x, int y) {
        return x >= 0 && y >= 0 && x < this.columns.length && y < this.height;
    }
}
