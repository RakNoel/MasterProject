package tools;

import java.util.ArrayList;
import java.util.BitSet;

public class Kernel {
    int r, k;

    public Kernel(int k, int r) {
        this.k = k;
        this.r = r;
    }

    /**
     * Could be shorted and maybe included into partitioning
     *
     * @param bnm
     * @return
     */
    public BinaryMatrix ColumnReduce(BinaryMatrix bnm) {
        ArrayList<BitSet> set = new ArrayList<>();
        for (int i = 0; i < bnm.getWidth(); i++) {
            int eq = 0;
            for (int j = i + 1; j < bnm.getWidth(); j++)
                if (bnm.getColumn(i).equals(bnm.getColumn(j)))
                    eq++;
            if (eq <= k + 2) set.add(bnm.getColumn(i));
        }
        return new BinaryMatrix(set, bnm.getHeight());
    }

    public BinaryMatrix RowReduce(BinaryMatrix bnm) {
        //TODO finish
        ArrayList<BitSet> set = new ArrayList<>();
        for (int i = 0; i < bnm.getHeight(); i++)
            if (bnm.getRow(i).cardinality() != 0 && bnm.getRow(i).cardinality() != bnm.getWidth())
                set.add(bnm.getRow(i));
            return null;
    }

    public ArrayList<BinaryMatrix> Partition(BinaryMatrix bnm) {
        //TODO finish
        return null;
    }
}

