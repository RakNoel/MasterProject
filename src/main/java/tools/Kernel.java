package tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Kernel {
    private int r, k;

    public Kernel(int k, int r) {
        this.k = k;
        this.r = r;
    }

    public void ColumnReduce(BinaryMatrix bnm) {
        for (int i = 0, eq = 0; i < bnm.getWidth(); i++, eq = 0)
            for (int j = i + 1; j < bnm.getWidth(); j++)
                if (BinaryMatrix.hammingDistance(bnm.getColumn(i), bnm.getColumn(j)) == 0) if (++eq > k + 2)
                    bnm.deleteColumn(j--);
    }

    public void RowReduce(BinaryMatrix bnm) {
        for (int i = 0; i < bnm.getHeight(); i++)
            if (bnm.getRow(i).cardinality() == 0 || bnm.getRow(i).cardinality() == bnm.getWidth())
                bnm.deleteRow(i--);
    }

    public ArrayList<BinaryMatrix> Partition(BinaryMatrix bnm) {
        //TODO finish
        return null;
    }
}

