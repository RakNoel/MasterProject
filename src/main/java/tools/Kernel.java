package tools;

import java.util.ArrayList;
import java.util.BitSet;

public class Kernel {
    private int r, k;

    public Kernel(int k, int r) {
        this.k = k;
        this.r = r;
    }

    public void ColumnReduce(BinaryMatrix bnm) {
        for (int i = 0, eq = 0; i < bnm.getWidth(); i++, eq = 0)
            for (int j = i + 1; j < bnm.getWidth(); j++)
                if (BinaryMatrix.hammingDistance(bnm.getColumn(i), bnm.getColumn(j)) == 0)
                    if (++eq > k + 2)
                        bnm.deleteColumn(j--);
    }

    public void RowReduce(BinaryMatrix bnm) {
        for (int i = 0; i < bnm.getHeight(); i++)
            if (bnm.getRow(i).cardinality() == 0 || bnm.getRow(i).cardinality() == bnm.getWidth())
                bnm.deleteRow(i--);
    }

    public ArrayList<BinaryMatrix> Partition(BinaryMatrix bnm) throws BinaryMatrixNoInstanceException {
        ArrayList<ArrayList<BitSet>> S = new ArrayList<>();
        while (bnm.getWidth() > 0) {
            if (S.size() > r) throw new BinaryMatrixNoInstanceException("t -ge r");

            var working = new ArrayList<BitSet>();
            var tmp = bnm.getColumn(0);
            working.add(tmp);
            bnm.deleteColumn(0);

            for (int i = 0; i < bnm.getWidth(); i++)
                if (BinaryMatrix.hammingDistance(bnm.getColumn(i), tmp) <= k) {
                    working.add(bnm.getColumn(i));
                    bnm.deleteColumn(i);
                }

            S.add(working);
        }

        ArrayList<BinaryMatrix> result = new ArrayList<>(S.size());
        for (var s : S) result.add(new BinaryMatrix(s, bnm.getHeight()));
        return result;
    }

    public ArrayList<BinaryMatrix> Kernelize(BinaryMatrix bnm) throws BinaryMatrixNoInstanceException {
        ArrayList<BinaryMatrix> res = new ArrayList<>();

        this.ColumnReduce(bnm);
        var partitions = Partition(bnm);
        for (var a_t : partitions) {
            RowReduce(a_t);
            if (a_t.getHeight() > 0) res.add(a_t); //TODO: Should this be done?
        }
        return res;
    }
}

