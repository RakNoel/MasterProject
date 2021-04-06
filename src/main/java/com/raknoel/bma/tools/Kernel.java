package com.raknoel.bma.tools;

import com.raknoel.bma.exceptions.BinaryMatrixNoInstanceException;
import com.raknoel.bma.structures.BinaryMatrix;
import com.raknoel.bma.structures.BinarySubMatrix;

import java.util.ArrayList;
import java.util.List;

import static com.raknoel.bma.tools.HammingDistance.hammingDistance;

public class Kernel {
    private final int r;
    private final int k;

    public Kernel(int k, int r) {
        this.k = k;
        this.r = r;
    }

    public void columnReduce(BinarySubMatrix colReducedMatrix) {
        for (int i = 0; i < colReducedMatrix.getWidth(); i++) {
            int eq = 0;
            for (int j = i + 1; j < colReducedMatrix.getWidth(); j++)
                if (hammingDistance(colReducedMatrix.getColumn(i), colReducedMatrix.getColumn(j)) == 0) {
                    eq++;
                    if (eq > k + 2)
                        colReducedMatrix.deleteColumn(j--);
                }
        }
    }

    public void rowReduce(BinarySubMatrix bnm) {
        for (int i = 0; i < bnm.getHeight(); i++)
            if (bnm.getRow(i).cardinality() == 0 || bnm.getRow(i).cardinality() == bnm.getWidth())
                bnm.deleteRow(i--);
    }

    public List<BinarySubMatrix> partition(BinarySubMatrix bnm) throws BinaryMatrixNoInstanceException {
        ArrayList<BinarySubMatrix> result = new ArrayList<>();
        while (bnm.getWidth() > 0) {
            if (result.size() > r - 1) throw new BinaryMatrixNoInstanceException("t -ge r");

            var working = new ArrayList<Integer>();
            var tmp = bnm.getColumn(0);
            working.add(bnm.getColumnID(0));
            bnm.deleteColumn(0);

            for (int i = 0; i < bnm.getWidth(); i++)
                if (hammingDistance(bnm.getColumn(i), tmp) <= k) {
                    working.add(bnm.getColumnID(i));
                    bnm.deleteColumn(i--);
                }

            result.add(new BinarySubMatrix(bnm.getParent(), working));
        }

        return result;
    }

    public List<BinarySubMatrix> kernelize(BinaryMatrix bnm) throws BinaryMatrixNoInstanceException {
        var reducedMatrix = new BinarySubMatrix(bnm);
        this.columnReduce(reducedMatrix);

        var partitions = partition(reducedMatrix);

        for (var a_t : partitions) {
            rowReduce(a_t);
            if (a_t.getHeight() <= 0)
                a_t.setFinished();
        }

        return partitions;
    }
}

